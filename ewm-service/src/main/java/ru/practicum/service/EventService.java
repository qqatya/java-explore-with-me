package ru.practicum.service;

import ru.practicum.dto.event.EventDetailDto;
import ru.practicum.dto.event.EventRequestDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.type.PublicationState;

import java.time.LocalDateTime;
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

    /**
     * Поиск событий администратором
     *
     * @param userIds     Идентификаторы инициаторов событий
     * @param states      Состояния публикации событий
     * @param categoryIds Идентификаторы категорий событий
     * @param rangeStart  Дата и время, не раньше которых должно произойти событие
     * @param rangeEnd    Дата и время, не позже которых должно произойти событие
     * @param from        Начальный элемент
     * @param size        Количество отображаемых элементов
     * @return Список событий
     */
    List<EventDetailDto> find(List<Long> userIds, List<PublicationState> states, List<Long> categoryIds,
                              LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    /**
     * Изменение события администратором
     *
     * @param eventId Идентификатор события
     * @param dto     Объект, содержащий информацию о событии
     * @return Измененное событие
     */
    EventDetailDto update(Long eventId, EventRequestDto dto);

}
