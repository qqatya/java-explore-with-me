package ru.practicum.service;

import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    /**
     * Сохранение информации о новом запросе
     *
     * @param dto Объект, содержащий данные о запросе
     */
    void create(HitRequestDto dto);

    /**
     * Получение статистики по посещениям
     *
     * @param start  Дата и время начала диапазона, за который нужно выгрузить статистику
     * @param end    Дата и время конца диапазона, за который нужно выгрузить статистику
     * @param uris   Список uri, для которых нужно выгрузить статистику
     * @param unique Нужно ли учитывать только уникальные посещения
     * @return Список объектов, содержащих данные о статистике
     */
    List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
