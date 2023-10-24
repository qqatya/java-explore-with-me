package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationRequestDto;
import ru.practicum.dto.compilation.CompilationDetailDto;
import ru.practicum.service.CompilationService;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Validated
public class AdminCompilationController {

    private final CompilationService compilationService;

    /**
     * Создание новой подборки
     *
     * @param dto Объект, содержащий данные о подборке
     * @return Созданная подборка
     */
    @PostMapping
    @ResponseStatus(CREATED)
    public CompilationDetailDto create(@Valid @RequestBody CompilationRequestDto dto) {
        return compilationService.create(dto);
    }

    /**
     * Удаление подборки
     *
     * @param id Идентификатор подборки
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        compilationService.delete(id);
    }

    /**
     * Обновление подборки
     *
     * @param id Идентификатор подборки
     * @return Обновленная подборка
     */
    @PatchMapping("/{id}")
    public CompilationDetailDto update(@PathVariable Long id,  @Valid @RequestBody CompilationRequestDto dto) {
        return compilationService.update(id, dto);
    }
}
