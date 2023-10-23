package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.compilation.CompilationRequestDto;
import ru.practicum.dto.compilation.CompilationResponseDto;
import ru.practicum.entity.Compilation;
import ru.practicum.entity.Event;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.CompilationService;
import ru.practicum.service.EventService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.exception.type.ExceptionType.COMPILATION_NOT_FOUND;
import static ru.practicum.exception.type.ExceptionType.USER_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    private final CompilationMapper compilationMapper;

    private final EventRepository eventRepository;

    private final EventService eventService;

    @Override
    public CompilationResponseDto create(CompilationRequestDto dto) {
        Compilation compilation = compilationRepository.save(compilationMapper.mapToEntity(dto));
        log.info("created compilation: {}", compilation);

        List<Long> eventIds = dto.getEvents();
        List<Event> events = new ArrayList<>();

        if (eventIds != null && !eventIds.isEmpty()) {
            log.info("setting compilation ot eventIds = {}", eventIds);
            events = eventIds.stream()
                    .map(id -> eventRepository.findById(id)
                            .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND.getValue(), id))))
                    .map(e -> {
                        e.setCompilation(compilation);
                        return eventRepository.save(e);
                    }).collect(Collectors.toList());
        }
        return compilationMapper.mapToDto(compilation, eventService.mapToShortDtos(events));
    }

    @Override
    public void delete(Long id) {
        Compilation compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(COMPILATION_NOT_FOUND.getValue(), id)));

        compilationRepository.delete(compilation);
        log.info("compilationId = {} has been deleted", id);

    }

    @Override
    public CompilationResponseDto update(Long id, CompilationRequestDto dto) {
        Compilation existing = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(COMPILATION_NOT_FOUND.getValue(), id)));
        Compilation updated = compilationRepository.save(compilationMapper.mapToEntityForUpdate(existing, dto, id));
        List<Long> eventIds = dto.getEvents();
        List<Event> events = new ArrayList<>();

        if (eventIds != null && !eventIds.isEmpty()) {
            events = updateConnectedEvents(eventIds, updated);
        }
        log.info("updated compilationId = {}", id);
        return compilationMapper.mapToDto(updated, eventService.mapToShortDtos(events));
    }

    private List<Event> updateConnectedEvents(List<Long> eventIds, Compilation updated) {
        List<Event> events = new ArrayList<>();
        List<Event> existingEvents = eventRepository.findByCompilationIdIn(eventIds);
        for (Event e : existingEvents) {
            if (!eventIds.contains(e.getId())) {
                e.setCompilation(null);
                eventIds.remove(e.getId());
            } else {
                events.add(e);
            }
        }
        events.addAll(eventIds.stream().map(i -> eventRepository.findById(i)
                        .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND.getValue(), i))))
                .map(e -> {
                    e.setCompilation(updated);
                    return eventRepository.save(e);
                }).collect(Collectors.toList()));
        return events;
    }
}
