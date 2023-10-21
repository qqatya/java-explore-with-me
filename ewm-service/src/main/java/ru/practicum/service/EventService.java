package ru.practicum.service;

import ru.practicum.dto.event.EventDetailDto;
import ru.practicum.dto.event.EventRequestDto;
import ru.practicum.dto.event.EventShortDto;

import java.util.List;

public interface EventService {

    /**
     * Получение событий, добавленных текущим пользователем
     *
     * @param userId Идентификатор пользователя
     * @param from   Начальный элемент
     * @param size   Количество отображаемых элементов
     * @return Список событий
     */
    List<EventShortDto> findByUserId(Long userId, Integer from, Integer size);

    /**
     * Добавление нового события
     *
     * @param userId Идентификатор пользователя
     * @param dto    Объект, содержащий информацию о событии
     * @return Добавленное событие
     */
    EventDetailDto create(Long userId, EventRequestDto dto);

    /**
     * Получение полной информации о событии, добавленном текущим пользователем
     *
     * @param userId  Идентификатор пользователя
     * @param eventId Идентификатор события
     * @return Полная информация о событии
     */
    EventDetailDto getDetailInfo(Long userId, Long eventId);

    /**
     * Изменение события, добавленного текущим пользователем
     *
     * @param userId  Идентификатор пользователя
     * @param eventId Идентификатор события
     * @param dto     Объект, содержащий информацию о событии
     * @return Измененное событие
     */
    EventDetailDto update(Long userId, Long eventId, EventRequestDto dto);

}
