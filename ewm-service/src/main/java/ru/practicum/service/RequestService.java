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
    List<RequestDto> getRequests(Long userId, Long eventId);


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

}
