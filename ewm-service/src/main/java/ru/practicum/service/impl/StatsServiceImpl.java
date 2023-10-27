package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsClient client;

    @Override
    public Long getViews(LocalDateTime start, LocalDateTime end, List<String> uris) {
        ResponseEntity<Object> response = client.getStats(start, end, uris, true);
        List<StatsDto> dtos = null;
        if (response.getBody() != null) {
            List<Map<String, Object>> responseBody = (List<Map<String, Object>>) response.getBody();

            dtos = getStats(responseBody);
        }
        int uriIdx = 0;

        return dtos == null || dtos.isEmpty() ? 0 : dtos.get(uriIdx).getHits();
    }

    @Override
    public void create(HitRequestDto dto) {
        log.info("creating new stats: {}", dto);
        client.create(dto);
    }

    private List<StatsDto> getStats(List<Map<String, Object>> responseBody) {
        List<StatsDto> result = new ArrayList<>();

        for (Map<String, Object> map : responseBody) {
            StatsDto dto = StatsDto.builder()
                    .app((String) map.get("app"))
                    .uri((String) map.get("uri"))
                    .hits(((Integer) map.get("hits")).longValue())
                    .build();

            result.add(dto);
        }
        return result;
    }

}
