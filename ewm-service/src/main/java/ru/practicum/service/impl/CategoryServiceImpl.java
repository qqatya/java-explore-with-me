package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CategoryDto;
import ru.practicum.entity.Category;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.service.CategoryService;

import static ru.practicum.exception.type.ExceptionType.CATEGORY_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto create(CategoryDto dto) {
        Category category = categoryRepository.save(categoryMapper.mapToEntity(dto));

        log.info("new category {} has been created", category);
        return categoryMapper.mapToDto(category);
    }

    @Override
    public CategoryDto update(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_FOUND.getValue(), id)));

        log.info("found category by id = {}", id);
        return categoryMapper.mapToDto(category);
    }

    @Override
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_FOUND.getValue(), id)));

        log.info("deleting category by id = {}", id);
        //TODO: Добавить логику проверки на наличие событий, связанных с этой категорией
        categoryRepository.delete(category);
    }
}
