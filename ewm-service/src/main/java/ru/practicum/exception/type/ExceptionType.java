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
    CATEGORY_HAS_EVENTS("Category with id = %s has events"),
    REQUEST_DUPLICATE("Request with requesterId = %s and eventId = %s already exists"),
    EVENT_INITIATED_BY_REQUESTER("Cannot create requests from event initiator"),
    REQUEST_FOR_UNPUBLISHED_EVENT("Requests for unpublished events are not accepted"),
    REQUEST_ALREADY_CANCELED("Request with id = %s was already canceled"),
    COMPILATION_NOT_FOUND("Compilation with id = %s was not found"),
    STATE_NOT_FOUND("Publication state not found for event action = %s"),
    ALREADY_SUBSCRIBED("UserId = %s is already subscribed on userId = %s"),
    NOT_SUBSCRIBED("UserId = %s has to be subscribed on userIds = %s");

    private final String value;
}
