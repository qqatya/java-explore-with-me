package ru.practicum.dto;

import lombok.Data;

@Data
public class StatsDto {

    private String app;

    private String uri;

    private Long hits;
}
