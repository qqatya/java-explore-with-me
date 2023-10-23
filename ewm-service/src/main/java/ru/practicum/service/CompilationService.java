package ru.practicum.service;

import ru.practicum.dto.compilation.CompilationRequestDto;
import ru.practicum.dto.compilation.CompilationResponseDto;

public interface CompilationService {

    /**
     * Создание новой подборки
     *
     * @param dto Объект, содержащий данные о подборке
     * @return Созданная подборка
     */
    CompilationResponseDto create(CompilationRequestDto dto);

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
    CompilationResponseDto update(Long id, CompilationRequestDto dto);

}
