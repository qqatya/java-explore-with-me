package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.entity.Endpoint;
import ru.practicum.entity.Hit;

@Component
public class HitMapper {

    public Hit mapToEntity(HitRequestDto dto, Endpoint endpoint) {
        return Hit.builder()
                .endpoint(endpoint)
                .ipAddress(dto.getIp())
                .sentDttm(dto.getTimestamp())
                .build();
    }

}
