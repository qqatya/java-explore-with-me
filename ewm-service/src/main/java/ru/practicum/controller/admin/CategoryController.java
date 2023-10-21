package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.service.CategoryService;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Добавление новой категории
     *
     * @param dto Объект, содержащий данные о категории
     * @return Созданная категория
     */
    @PostMapping
    @ResponseStatus(CREATED)
    public CategoryDto create(@Valid @RequestBody CategoryDto dto) {
        return categoryService.create(dto);
    }

    /**
     * Изменение категории
     *
     * @param id Идентификатор категории
     * @return Измененная категория
     */
    @PatchMapping("/{id}")
    public CategoryDto update(@PathVariable("id") Long id, @Valid @RequestBody CategoryDto dto) {
        return categoryService.update(id, dto);
    }

    /**
     * Удаление категории
     *
     * @param id Идентификатор категории
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        categoryService.delete(id);
    }
}
