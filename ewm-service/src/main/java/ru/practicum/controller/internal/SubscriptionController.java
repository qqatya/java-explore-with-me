package ru.practicum.controller.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.UserSubscriptionDto;
import ru.practicum.service.UserService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/users/{subscriberId}/subscriptions/{userId}")
@RequiredArgsConstructor
public class SubscriptionController {

    private final UserService userService;

    /**
     * Добавление подписки на пользователя
     *
     * @param subscriberId Идентификатор текущего пользователя
     * @param userId       Идентификатор пользователя, на которого оформляется подписка
     * @return Объект, содержащий данные о пользователе и его подписках
     */
    @PostMapping
    @ResponseStatus(CREATED)
    public UserSubscriptionDto create(@PathVariable Long subscriberId, @PathVariable Long userId) {
        return userService.subscribe(subscriberId, userId);
    }

    /**
     * Удаление подписки на пользователя
     *
     * @param subscriberId Идентификатор текущего пользователя
     * @param userId       Идентификатор пользователя, от которого текущий отписывается
     * @return Объект, содержащий данные о пользователе и его подписках
     */
    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    public UserSubscriptionDto delete(@PathVariable Long subscriberId, @PathVariable Long userId) {
        return userService.unsubscribe(subscriberId, userId);
    }
}
