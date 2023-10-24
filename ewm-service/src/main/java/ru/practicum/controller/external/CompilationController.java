package ru.practicum.controller.external;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDetailDto;
import ru.practicum.service.CompilationService;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@Validated
@RequiredArgsConstructor
public class CompilationController {

    private final CompilationService compilationService;

    /**
     * Поиск подборок событий
     *
     * @param pinned Параметр для поиска закрепленных/не закрепленных подборок
     * @param from   Начальный элемент
     * @param size   Количество отображаемых элементов
     * @return Список подборок
     */
    @GetMapping
    public List<CompilationDetailDto> find(@RequestParam(required = false, defaultValue = "false") Boolean pinned,
                                           @PositiveOrZero @RequestParam(required = false, defaultValue = "0")
                                           Integer from,
                                           @PositiveOrZero @RequestParam(required = false, defaultValue = "10")
                                           Integer size) {
        return compilationService.find(pinned, from, size);
    }

    /**
     * Поиск подборки событий по идентификатору
     *
     * @param id Идентификатор подборки
     * @return Подборка
     */
    @GetMapping("/{id}")
    public CompilationDetailDto findById(@PathVariable Long id) {
        return compilationService.findById(id);
    }

}
