package ru.practicum.dto.compilation;

import lombok.*;
import ru.practicum.dto.event.EventShortDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationResponseDto {

    private Long id;

    private String title;

    private Boolean pinned;

    private List<EventShortDto> events;

}
