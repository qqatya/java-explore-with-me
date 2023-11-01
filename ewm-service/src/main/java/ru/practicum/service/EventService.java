package ru.practicum.service;

import ru.practicum.dto.event.EventCreateDto;
import ru.practicum.dto.event.EventDetailDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.EventUpdateDto;
import ru.practicum.dto.type.EventSort;
import ru.practicum.dto.type.PublicationState;
import ru.practicum.entity.Event;

import javax.servlet.http.HttpServletRequest;
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
    EventDetailDto create(Long userId, EventCreateDto dto);

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
    EventDetailDto updateByUser(Long userId, Long eventId, EventUpdateDto dto);

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
    List<EventDetailDto> findByAdmin(List<Long> userIds, List<PublicationState> states, List<Long> categoryIds,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    /**
     * Изменение события администратором
     *
     * @param eventId Идентификатор события
     * @param dto     Объект, содержащий информацию о событии
     * @return Измененное событие
     */
    EventDetailDto updateByAdmin(Long eventId, EventUpdateDto dto);

    /**
     * Преобразование событий в объекты с краткой информацией
     *
     * @param events События
     * @return Список объектов с краткой информации о событиях
     */
    List<EventShortDto> mapToShortDtos(List<Event> events);

    /**
     * Поиск событий с возможностью фильтрации
     *
     * @param categoryIds   Идентификаторы категорий событий
     * @param rangeStart    Дата и время, не раньше которых должно произойти событие
     * @param rangeEnd      Дата и время, не позже которых должно произойти событие
     * @param from          Начальный элемент
     * @param size          Количество отображаемых элементов
     * @param text          Текст для поиска в содержимом аннотации и подробном описании события
     * @param paid          Поиск только платных/бесплатных событий
     * @param onlyAvailable Признак поиска событий с неисчерпанным лимитом запросов на участие
     * @param sort          Параметр сортировки
     * @param request       Данные о запросе
     * @return Список событий
     */
    List<EventShortDto> find(String text, List<Long> categoryIds, Boolean paid, LocalDateTime rangeStart,
                             LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort, Integer from, Integer size,
                             HttpServletRequest request);

    /**
     * Поиск опубликованного события по идентификатору
     *
     * @param id      Идентификатор события
     * @param request Данные о запросе
     * @return Событие
     */
    EventDetailDto findPublishedById(Long id, HttpServletRequest request);

    /**
     * Получение пользователем списка актуальных событий пользователей, на которых он подписан
     *
     * @param subscriberId Идентификатор текущего пользователя (подписчика)
     * @param publisherIds Идентификаторы пользователей, на которых текущий подписан
     * @param from         Начальный элемент
     * @param size         Количество отображаемых элементов
     * @return Список событий
     */
    List<EventShortDto> findByPublisherIds(Long subscriberId, List<Long> publisherIds, Integer from, Integer size);

}
