package ru.practicum.service;

import ru.practicum.dto.CategoryDto;

import java.util.List;

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

    /**
     * Поиск категорий событий
     *
     * @param from Начальный элемент
     * @param size Количество отображаемых элементов
     * @return Список категорий
     */
    List<CategoryDto> find(Integer from, Integer size);

    /**
     * Поиск категории событий по идентификатору
     *
     * @param id Идентификатор категории
     * @return Категория
     */
    CategoryDto findById(Long id);
}
