package ru.practicum.service;

import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserSubscriptionDto;

import java.util.List;

public interface UserService {

    /**
     * Получение информации о пользователях
     *
     * @param ids Список идентификаторов пользователей
     * @return Список пользователей
     */
    List<UserDto> findByIds(List<Long> ids);

    /**
     * Получение информации о пользователях
     *
     * @param from Начальный элемент
     * @param size Количество отображаемых элементов
     * @return Список пользователей
     */
    List<UserDto> find(Integer from, Integer size);

    /**
     * Добавление нового пользователя
     *
     * @param dto Объект, содержащий данные о пользователе
     * @return Созданный пользователь
     */
    UserDto create(UserDto dto);

    /**
     * Удаление пользователя
     *
     * @param id Идентификатор пользователя
     */
    void delete(Long id);

    /**
     * Добавление подписки на пользователя
     *
     * @param subscriberId Идентификатор текущего пользователя
     * @param userId       Идентификатор пользователя, на которого оформляется подписка
     * @return Объект, содержащий данные о пользователе и его подписках
     */
    UserSubscriptionDto subscribe(Long subscriberId, Long userId);

    /**
     * Удаление подписки на пользователя
     *
     * @param subscriberId Идентификатор текущего пользователя
     * @param userId       Идентификатор пользователя, от которого текущий отписывается
     * @return Объект, содержащий данные о пользователе и его подписках
     */
    UserSubscriptionDto unsubscribe(Long subscriberId, Long userId);

}
