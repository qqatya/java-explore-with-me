package ru.practicum.service;

import ru.practicum.dto.compilation.CompilationRequestDto;
import ru.practicum.dto.compilation.CompilationDetailDto;

import java.util.List;

public interface CompilationService {

    /**
     * Создание новой подборки
     *
     * @param dto Объект, содержащий данные о подборке
     * @return Созданная подборка
     */
    CompilationDetailDto create(CompilationRequestDto dto);

    /**
     * Удаление подборки
     *
     * @param id Идентификатор подборки
     */
    void delete(Long id);

    /**
     * Обновление подборки
     *
     * @param id  Идентификатор подборки
     * @param dto Объект, содержащий данные о подборке
     * @return Обновленная подборка
     */
    CompilationDetailDto update(Long id, CompilationRequestDto dto);

    /**
     * Поиск подборок событий
     *
     * @param pinned Параметр для поиска закрепленных/не закрепленных подборок
     * @param from   Начальный элемент
     * @param size   Количество отображаемых элементов
     * @return Список подборок
     */
    List<CompilationDetailDto> find(Boolean pinned, Integer from, Integer size);

    /**
     * Поиск подборки событий по идентификатору
     *
     * @param id Идентификатор подборки
     * @return Подборка
     */
    CompilationDetailDto findById(Long id);

}
