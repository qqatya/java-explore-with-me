package ru.practicum.service;

import ru.practicum.dto.event.request.RequestDto;
import ru.practicum.dto.event.request.RequestStatusDto;
import ru.practicum.dto.event.request.RequestStatusUpdateDto;

import java.util.List;

public interface RequestService {

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     *
     * @param userId  Идентификатор пользователя
     * @param eventId Идентификатор события
     * @return Список запросов на участие
     */
    List<RequestDto> findRequests(Long userId, Long eventId);


    /**
     * Изменение статуса запросов на участие в событии текущего пользователя
     *
     * @param userId  Идентификатор пользователя
     * @param eventId Идентификатор события
     * @param dto     Объект, содержащий данные для изменения статуса
     * @return Объект, содержащий списки подтвержденных и отклоненных запросов
     */
    RequestStatusDto changeStatus(Long userId, Long eventId, RequestStatusUpdateDto dto);

    /**
     * Подсчет количества подтвержденных запросов
     *
     * @param eventId Идентификатор события
     * @return Количество
     */
    Integer findConfirmedRequestsAmount(Long eventId);

    /**
     * Получение информации о запросах текущего пользователя на участие в чужих событиях
     *
     * @param userId Идентификатор текущего пользователя
     * @return Список запросов
     */
    List<RequestDto> findRequests(Long userId);

    /**
     * Добавление запроса от текущего пользователя на участие в событии
     *
     * @param requesterId Идентификатор текущего пользователя
     * @param eventId     Идентификатор события
     * @return Созданный запрос
     */
    RequestDto create(Long requesterId, Long eventId);

    /**
     * Отмена запроса на участие в событии
     *
     * @param requesterId Идентификатор текущего пользователя
     * @param requestId   Идентификатор заявки
     * @return Объект, содержащий информацию об отмене
     */
    RequestDto cancel(Long requesterId, Long requestId);
}
