package ru.practicum.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.EventDetailDto;
import ru.practicum.dto.event.EventRequestDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.location.LocationDto;
import ru.practicum.dto.type.PublicationState;
import ru.practicum.entity.*;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.EventService;
import ru.practicum.service.LocationService;
import ru.practicum.service.RequestService;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.dto.type.PublicationState.PENDING;
import static ru.practicum.dto.type.PublicationState.PUBLISHED;
import static ru.practicum.dto.type.PublicationStateAction.PUBLISH_EVENT;
import static ru.practicum.dto.type.PublicationStateAction.REJECT_EVENT;
import static ru.practicum.exception.type.ExceptionType.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final LocationService locationService;

    private final RequestService requestService;

    private final StatsService statsService;

    @Override
    public List<EventShortDto> findByUserId(Long userId, Integer from, Integer size) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(String.format(USER_NOT_FOUND.getValue(), userId));
        }
        log.info("searching for events by userId = {}. from = {}, size = {}", userId, from, size);
        int page = from != 0 ? from / size : from;
        Pageable pageable = PageRequest.of(page, size);
        List<Event> events = eventRepository.findByInitiatorId(userId, pageable);

        log.info("found {} events", events.size());
        return mapToShortDtos(events);
    }

    @Override
    public EventDetailDto create(Long userId, EventRequestDto dto) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND.getValue(), userId)));
        Long categoryId = dto.getCategory();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_FOUND.getValue(), categoryId)));
        Location location = locationService.findOrCreateLocation(dto.getLocation());
        Event event = eventRepository.save(eventMapper.mapToEntity(dto, category, location, initiator));

        log.info("created new event: {}", event);
        return mapToDetailDto(event);
    }

    @Override
    public EventDetailDto getDetailInfo(Long userId, Long eventId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(String.format(USER_NOT_FOUND.getValue(), userId));
        }
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND.getValue(), eventId)));

        log.info("found event: {}", event);
        return mapToDetailDto(event);
    }

    @Override
    public EventDetailDto update(Long userId, Long eventId, EventRequestDto dto) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND.getValue(), userId)));
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND.getValue(), eventId)));

        if (PUBLISHED.equals(event.getPublicationState()) || isEventDateWithinTwoHours(event.getEventDate())) {
            throw new SecurityException(EVENT_UNAVAILABLE_FOR_EDITING.getValue());
        }
        Category category = getCategoryForUpdate(dto.getCategory(), event.getCategory());
        Location location = getLocationForUpdate(dto.getLocation(), event.getLocation());

        Event updated = eventRepository.save(eventMapper
                .mapToEntityForUpdate(event, dto, category, location, initiator));
        log.info("event: {} has been updated", updated);

        return mapToDetailDto(updated);
    }

    @Override
    public List<EventDetailDto> find(List<Long> userIds, List<PublicationState> states, List<Long> categoryIds,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        log.info("searching for events by userIds = {}, states = {}, categoryIds = {}, rangeStart = {}, rangeEnd = {}, "
                + "from = {}, size = {}", userIds, states, categoryIds, rangeStart, rangeEnd, from, size);
        BooleanExpression userIdsIn = QEvent.event.initiator.id.in(userIds);
        BooleanExpression publicationStatesIn = QEvent.event.publicationState.in(states);
        BooleanExpression categoryIdsIn = QEvent.event.category.id.in(categoryIds);
        BooleanExpression eventDateAfterOrEq = QEvent.event.eventDate.goe(rangeStart);
        BooleanExpression eventDateBeforeOrEq = QEvent.event.eventDate.loe(rangeEnd);
        int page = from != 0 ? from / size : from;
        Pageable pageable = PageRequest.of(page, size);
        BooleanBuilder filters = new BooleanBuilder();

        if (userIds != null && !userIds.isEmpty()) {
            filters.and(userIdsIn);
        }
        if (states != null && !states.isEmpty()) {
            filters.and(publicationStatesIn);
        }
        if (categoryIds != null && !categoryIds.isEmpty()) {
            filters.and(categoryIdsIn);
        }
        if (rangeStart != null) {
            filters.and(eventDateAfterOrEq);
        }
        if (rangeEnd != null) {
            filters.and(eventDateBeforeOrEq);
        }
        List<Event> events = eventRepository.findAll(filters, pageable).getContent();

        log.info("found {} events", events.size());
        return events.stream().map(this::mapToDetailDto).collect(Collectors.toList());
    }

    @Override
    public EventDetailDto update(Long eventId, EventRequestDto dto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND.getValue(), eventId)));

        if (REJECT_EVENT.equals(dto.getStateAction()) && PUBLISHED.equals(event.getPublicationState())) {
            throw new IllegalStateException(String.format(EVENT_ALREADY_PUBLISHED.getValue(), eventId));
        }
        if (PUBLISH_EVENT.equals(dto.getStateAction()) && !PENDING.equals(event.getPublicationState())) {
            throw new IllegalStateException(EVENT_MUST_BE_PENDING.getValue());
        }
        LocalDateTime publicationDate = event.getPublicationDate() != null ? event.getPublicationDate()
                : LocalDateTime.now();

        if (!eventDateIsAfterOrEqHourBeforePublicationDate(event.getEventDate(), publicationDate)) {
            throw new IllegalStateException(EVENT_UNAVAILABLE_FOR_EDITING_ADMIN.getValue());
        }
        Category category = getCategoryForUpdate(dto.getCategory(), event.getCategory());
        Location location = getLocationForUpdate(dto.getLocation(), event.getLocation());

        Event updated = eventRepository.save(eventMapper
                .mapToEntityForUpdate(event, dto, category, location, event.getInitiator()));
        log.info("event: {} has been updated", updated);

        return mapToDetailDto(updated);
    }

    private boolean isEventDateWithinTwoHours(LocalDateTime eventDate) {
        LocalDateTime withinTwoHours = LocalDateTime.now().plusHours(2);

        return withinTwoHours.equals(eventDate) || withinTwoHours.isAfter(eventDate);
    }

    private Category getCategoryForUpdate(Long categoryId, Category existing) {
        return categoryId != null ? categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_FOUND.getValue(), categoryId)))
                : existing;
    }

    private Location getLocationForUpdate(LocationDto dto, Location existing) {
        return dto != null ? locationService.findOrCreateLocation(dto)
                : existing;
    }

    private List<EventShortDto> mapToShortDtos(List<Event> events) {
        return events.stream()
                .map(e -> {
                    Long views = statsService.getViews(e.getCreateDate(), LocalDateTime.now(), createUris(e.getId()));

                    return eventMapper.mapToShortDto(e, requestService.findConfirmedRequestsAmount(e.getId()), views);
                })
                .collect(Collectors.toList());
    }

    private EventDetailDto mapToDetailDto(Event event) {
        Long views = statsService.getViews(event.getCreateDate(), LocalDateTime.now(), createUris(event.getId()));

        return eventMapper.mapToDetailDto(event, requestService.findConfirmedRequestsAmount(event.getId()), views);
    }

    private List<String> createUris(Long eventId) {
        return List.of("/events/" + eventId);
    }

    private boolean eventDateIsAfterOrEqHourBeforePublicationDate(LocalDateTime eventDate,
                                                                  LocalDateTime publicationDate) {
        LocalDateTime plusHour = publicationDate.plusHours(1);
        return eventDate.equals(plusHour) || eventDate.isAfter(plusHour);
    }

}
