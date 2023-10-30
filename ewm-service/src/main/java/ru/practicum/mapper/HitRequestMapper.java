package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.HitRequestDto;

import java.time.LocalDateTime;

@Component
public class HitRequestMapper {

    private static final String APP = "ewm-service";

    public HitRequestDto mapToDto(String uri, String ip) {
        return HitRequestDto.builder()
                .app(APP)
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
