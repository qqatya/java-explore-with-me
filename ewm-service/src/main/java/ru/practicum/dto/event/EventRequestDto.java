package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.event.location.LocationDto;
import ru.practicum.dto.type.PublicationStateAction;
import ru.practicum.validation.CreateEvent;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDto {

    @NotBlank(groups = CreateEvent.class)
    @Min(value = 3)
    @Max(value = 120)
    private String title;

    @NotBlank(groups = CreateEvent.class)
    @Min(20)
    @Max(2000)
    private String annotation;

    @NotNull(groups = CreateEvent.class)
    private Long category;

    @NotBlank(groups = CreateEvent.class)
    @Min(20)
    @Max(7000)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(groups = CreateEvent.class)
    private LocalDateTime eventDate;

    @NotNull(groups = CreateEvent.class)
    @Valid
    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private PublicationStateAction stateAction;

}
