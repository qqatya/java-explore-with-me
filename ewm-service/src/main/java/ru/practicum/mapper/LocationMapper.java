package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.event.location.LocationDto;
import ru.practicum.entity.Location;

@Component
public class LocationMapper {

    public LocationDto mapToDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLatitude())
                .lon(location.getLongitude())
                .build();
    }

    public Location mapToEntity(LocationDto dto) {
        return Location.builder()
                .latitude(dto.getLat())
                .longitude(dto.getLon())
                .build();
    }

}
