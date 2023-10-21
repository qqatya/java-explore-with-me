package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.StatsDto;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsClient client;

    @Override
    public Long getViews(LocalDateTime start, LocalDateTime end, List<String> uris) {
        ResponseEntity<Object> response = client.getStats(start, end, uris, false);
        List<StatsDto> dtos = (List<StatsDto>) response.getBody();
        checkArgument(dtos != null, "stats are null");
        int uriIdx = 0;

        return dtos.get(uriIdx).getHits();
    }
}
