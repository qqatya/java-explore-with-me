package ru.practicum.service;

import ru.practicum.dto.event.location.LocationDto;
import ru.practicum.entity.Location;

public interface LocationService {

    /**
     * Поиск локации или создание, если таковая отсутствует
     *
     * @param dto Объект, содержащий данные о локации
     * @return Найденная/созданная локация
     */
    Location findOrCreateLocation(LocationDto dto);
}
