package ru.practicum.controller.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.request.RequestDto;
import ru.practicum.service.RequestService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    /**
     * Получение информации о запросах текущего пользователя на участие в чужих событиях
     *
     * @param userId Идентификатор текущего пользователя
     * @return Список запросов
     */
    @GetMapping
    public List<RequestDto> find(@PathVariable Long userId) {
        return requestService.findRequests(userId);
    }

    /**
     * Добавление запроса от текущего пользователя на участие в событии
     *
     * @param userId  Идентификатор текущего пользователя
     * @param eventId Идентификатор события
     * @return Созданный запрос
     */
    @PostMapping("/{eventId}")
    @ResponseStatus(CREATED)
    public RequestDto create(@PathVariable Long userId, @PathVariable Long eventId) {
        return requestService.create(userId, eventId);
    }

    /**
     * Отмена запроса на участие в событии
     *
     * @param userId    Идентификатор текущего пользователя
     * @param requestId Идентификатор заявки
     * @return Объект, содержащий информацию об отмене
     */
    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        return requestService.cancel(userId, requestId);
    }
}
