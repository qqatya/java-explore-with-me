package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.compilation.CompilationRequestDto;
import ru.practicum.dto.compilation.CompilationDetailDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.entity.Compilation;

import java.util.List;

@Component
public class CompilationMapper {

    public Compilation mapToEntity(CompilationRequestDto dto) {
        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.getPinned())
                .build();
    }

    public CompilationDetailDto mapToDto(Compilation compilation, List<EventShortDto> events) {
        return CompilationDetailDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(events)
                .build();
    }

    public Compilation mapToEntityForUpdate(Compilation existing, CompilationRequestDto dto, Long id) {
        return Compilation.builder()
                .id(id)
                .title(dto.getTitle() != null ? dto.getTitle() : existing.getTitle())
                .pinned(dto.getPinned() != null ? dto.getPinned() : existing.getPinned())
                .build();
    }
}
