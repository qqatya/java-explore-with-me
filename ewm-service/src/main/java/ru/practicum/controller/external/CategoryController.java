package ru.practicum.controller.external;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.service.CategoryService;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Поиск категорий событий
     *
     * @param from Начальный элемент
     * @param size Количество отображаемых элементов
     * @return Список категорий
     */
    @GetMapping
    public List<CategoryDto> find(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                  @PositiveOrZero @RequestParam(required = false, defaultValue = "10") Integer size) {
        return categoryService.find(from, size);
    }

    /**
     * Поиск категории событий по идентификатору
     *
     * @param id Идентификатор категории
     * @return Категория
     */
    @GetMapping("/{id}")
    public CategoryDto findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }


}
