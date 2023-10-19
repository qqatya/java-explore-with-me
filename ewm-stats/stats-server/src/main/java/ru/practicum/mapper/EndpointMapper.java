package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.entity.Endpoint;

@Component
public class EndpointMapper {

    public Endpoint mapToEntity(HitRequestDto dto) {
        return Endpoint.builder()
                .appName(dto.getApp())
                .uri(dto.getUri())
                .build();
    }

    public StatsDto mapToDto(Endpoint e, Long hitsCount) {
        return StatsDto.builder()
                .app(e.getAppName())
                .uri(e.getUri())
                .hits(hitsCount)
                .build();
    }


}
