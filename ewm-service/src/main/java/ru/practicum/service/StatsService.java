package ru.practicum.service;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    /**
     * Поиск просмотров
     *
     * @param start Дата и время начала диапазона, за который нужно выгрузить статистику
     * @param end   Дата и время конца диапазона, за который нужно выгрузить статистику
     * @param uris  Список uri, для которых нужно выгрузить статистику
     * @return Количество просмотров
     */
    Long getViews(LocalDateTime start, LocalDateTime end, List<String> uris);

}
