package ru.practicum.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionType {

    CATEGORY_NOT_FOUND("Category with id = %s was not found"),
    EMAIL_ALREADY_EXISTS("User with email = %s already exists"),
    USER_NOT_FOUND("User with id = %s was not found"),
    EVENT_NOT_FOUND("Event with id = %s was not found"),
    EVENT_UNAVAILABLE_FOR_EDITING("Events can be changed only more than 2 hours before event date "
            + "and must not be published yet"),
    REQUEST_STATUS_NOT_PENDING("Request with id = %s must have status PENDING"),
    REACHED_PARTICIPANT_LIMIT("The participant limit has been reached"),
    REQUEST_NOT_FOUND("Request with id = %s was not found"),
    EVENT_ALREADY_PUBLISHED("Cannot reject publication of already published event with id = %s"),
    EVENT_MUST_BE_PENDING("Cannot publish event with status not equal to PENDING"),
    EVENT_UNAVAILABLE_FOR_EDITING_ADMIN("Events can be changed only if event date is 1 or more hours less than "
            + "publication date"),
    CATEGORY_HAS_EVENTS("Category with id = %s has events");

    private final String value;
}
