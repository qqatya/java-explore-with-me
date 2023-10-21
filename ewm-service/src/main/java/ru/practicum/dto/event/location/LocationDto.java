package ru.practicum.dto.event.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.validation.CreateEvent;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

    @NotNull(groups = CreateEvent.class)
    private Double lat;

    @NotNull(groups = CreateEvent.class)
    private Double lon;

}
