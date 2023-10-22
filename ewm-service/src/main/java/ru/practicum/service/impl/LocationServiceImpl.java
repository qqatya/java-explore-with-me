package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.location.LocationDto;
import ru.practicum.entity.Location;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.repository.LocationRepository;
import ru.practicum.service.LocationService;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    private final LocationMapper locationMapper;

    @Override
    public Location findOrCreateLocation(LocationDto dto) {
        Location location = locationMapper.mapToEntity(dto);

        return locationRepository.save(location);
    }
}
