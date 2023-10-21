package ru.practicum.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.location.LocationDto;
import ru.practicum.entity.Location;
import ru.practicum.service.LocationService;

@Service
@Slf4j
public class LocationServiceImpl implements LocationService {

    @Override
    public Location findOrCreateLocation(LocationDto dto) {
        return null;
    }
}
