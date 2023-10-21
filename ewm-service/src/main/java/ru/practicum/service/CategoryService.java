package ru.practicum.service;

import ru.practicum.dto.CategoryDto;

public interface CategoryService {

    /**
     * Добавление новой категории
     *
     * @param dto Объект, содержащий данные о категории
     * @return Созданная категория
     */
    CategoryDto create(CategoryDto dto);

    /**
     * Изменение категории
     *
     * @param id  Идентификатор категории
     * @param dto Объект, содержащий данные о категории
     * @return Измененная категория
     */
    CategoryDto update(Long id, CategoryDto dto);

    /**
     * Удаление категории
     *
     * @param id Идентификатор категории
     */
    void delete(Long id);
}
