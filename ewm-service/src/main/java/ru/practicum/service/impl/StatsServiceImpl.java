package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsClient client;

    @Override
    public Map<Long, Long> getViews(LocalDateTime start, LocalDateTime end, List<String> uris) {
        ResponseEntity<Object> response = client.getStats(start, end, uris, true);
        checkArgument(response.getBody() != null, "response body is null");
        List<Map<String, Object>> responseBody = (List<Map<String, Object>>) response.getBody();

        return getStatsMap(responseBody);
    }

    @Override
    public void create(HitRequestDto dto) {
        log.info("creating new stats: {}", dto);
        client.create(dto);
    }

    private Map<Long, Long> getStatsMap(List<Map<String, Object>> responseBody) {
        Map<Long, Long> result = new HashMap<>();

        for (Map<String, Object> map : responseBody) {
            String uri = (String) map.get("uri");
            int idIdx = uri.length() - 1;
            Long eventId = Long.valueOf(uri.substring(idIdx));

            result.put(eventId, Long.valueOf((Integer) map.get("hits")));
        }
        return result;
    }

}
