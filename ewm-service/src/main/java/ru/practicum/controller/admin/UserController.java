package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.UserDto;
import ru.practicum.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RequestMapping("/admin/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Получение информации о пользователях
     *
     * @param ids  Список идентификаторов (для поиска конкретных пользователей)
     * @param from Начальный элемент
     * @param size Количество отображаемых элементов
     * @return Список пользователей
     */
    @GetMapping
    public List<UserDto> find(@RequestParam(required = false) List<Long> ids,
                              @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                              @PositiveOrZero @RequestParam(required = false, defaultValue = "10") Integer size) {
        if (ids != null && !ids.isEmpty()) {
            return userService.findByIds(ids);
        } else {
            return userService.find(from, size);
        }
    }

    /**
     * Добавление нового пользователя
     *
     * @param dto Объект, содержащий данные о пользователе
     * @return Созданный пользователь
     */
    @PostMapping
    @ResponseStatus(CREATED)
    public UserDto create(@Valid @RequestBody UserDto dto) {
        return userService.create(dto);
    }

    /**
     * Удаление пользователя
     *
     * @param id Идентификатор пользователя
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

}
