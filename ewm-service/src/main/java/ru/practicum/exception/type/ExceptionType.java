package ru.practicum.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionType {

    CATEGORY_NOT_FOUND("Category with id = %s was not found");

    private final String value;
}
