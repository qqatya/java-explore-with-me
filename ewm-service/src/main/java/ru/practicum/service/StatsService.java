package ru.practicum.service;

import ru.practicum.dto.HitRequestDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface StatsService {

    /**
     * Поиск просмотров
     *
     * @param start Дата и время начала диапазона, за который нужно выгрузить статистику
     * @param end   Дата и время конца диапазона, за который нужно выгрузить статистику
     * @param uris  Список uri, для которых нужно выгрузить статистику
     * @return Ключ - идентификатор события, значение - количество просмотров
     */
    Map<Long, Long> getViews(LocalDateTime start, LocalDateTime end, List<String> uris);

    /**
     * Сохранение информации о новом запросе
     *
     * @param dto Объект, содержащий данные о запросе
     */
    void create(HitRequestDto dto);

}
