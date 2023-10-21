package ru.practicum.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionType {

    CATEGORY_NOT_FOUND("Category with id = %s was not found"),
    EMAIL_ALREADY_EXISTS("User with email = %s already exists"),
    USER_NOT_FOUND("User with id = %s was not found");

    private final String value;
}
