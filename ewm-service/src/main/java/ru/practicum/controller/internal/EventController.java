package ru.practicum.controller.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventCreateDto;
import ru.practicum.dto.event.EventDetailDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.EventUpdateDto;
import ru.practicum.dto.event.request.RequestDto;
import ru.practicum.dto.event.request.RequestStatusDto;
import ru.practicum.dto.event.request.RequestStatusUpdateDto;
import ru.practicum.service.EventService;
import ru.practicum.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/users/{userId}/events")
@Validated
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    private final RequestService requestService;

    /**
     * Получение событий, добавленных текущим пользователем
     *
     * @param userId Идентификатор пользователя
     * @param from   Начальный элемент
     * @param size   Количество отображаемых элементов
     * @return Список событий
     */
    @GetMapping
    public List<EventShortDto> findByUserId(@PathVariable Long userId,
                                            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                            @PositiveOrZero @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventService.findByUserId(userId, from, size);
    }

    /**
     * Добавление нового события
     *
     * @param userId Идентификатор пользователя
     * @param dto    Объект, содержащий информацию о событии
     * @return Добавленное событие
     */
    @PostMapping
    @ResponseStatus(CREATED)
    public EventDetailDto create(@PathVariable Long userId,
                                 @Valid @RequestBody EventCreateDto dto) {
        return eventService.create(userId, dto);
    }

    /**
     * Получение полной информации о событии, добавленном текущим пользователем
     *
     * @param userId  Идентификатор пользователя
     * @param eventId Идентификатор события
     * @return Полная информация о событии
     */
    @GetMapping("/{eventId}")
    public EventDetailDto getDetailInfo(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getDetailInfo(userId, eventId);
    }

    /**
     * Изменение события, добавленного текущим пользователем
     *
     * @param userId  Идентификатор пользователя
     * @param eventId Идентификатор события
     * @param dto     Объект, содержащий информацию о событии
     * @return Измененное событие
     */
    @PatchMapping("/{eventId}")
    public EventDetailDto update(@PathVariable Long userId, @PathVariable Long eventId,
                                 @Valid @RequestBody(required = false) EventUpdateDto dto) {
        return eventService.updateByUser(userId, eventId, dto);
    }

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     *
     * @param userId  Идентификатор пользователя
     * @param eventId Идентификатор события
     * @return Список запросов на участие
     */
    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        return requestService.findRequests(userId, eventId);
    }

    /**
     * Изменение статуса запросов на участие в событии текущего пользователя
     *
     * @param userId  Идентификатор пользователя
     * @param eventId Идентификатор события
     * @param dto     Объект, содержащий данные для изменения статуса
     * @return Объект, содержащий списки подтвержденных и отклоненных запросов
     */
    @PatchMapping("/{eventId}/requests")
    public RequestStatusDto changeStatus(@PathVariable Long userId, @PathVariable Long eventId,
                                         @Valid @RequestBody RequestStatusUpdateDto dto) {
        return requestService.changeStatus(userId, eventId, dto);
    }

}
