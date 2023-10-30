package ru.practicum.service;

import ru.practicum.dto.user.UserDto;

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

}
