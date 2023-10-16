package ru.practicum.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class StatsServiceImpl implements StatsService {

    @Override
    public void create(HitRequestDto dto) {

    }

    @Override
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        return null;
    }
}
