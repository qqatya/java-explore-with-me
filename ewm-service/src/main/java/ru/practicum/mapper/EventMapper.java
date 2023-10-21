package ru.practicum.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.event.EventDetailDto;
import ru.practicum.dto.event.EventRequestDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.type.PublicationState;
import ru.practicum.dto.type.PublicationStateAction;
import ru.practicum.entity.Category;
import ru.practicum.entity.Event;
import ru.practicum.entity.Location;
import ru.practicum.entity.User;

import static ru.practicum.dto.type.PublicationState.CANCELED;
import static ru.practicum.dto.type.PublicationState.PUBLISHED;
import static ru.practicum.dto.type.PublicationStateAction.PUBLISH_EVENT;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final CategoryMapper categoryMapper;

    private final UserMapper userMapper;

    private final LocationMapper locationMapper;

    public EventShortDto mapToShortDto(Event event, Integer confirmedRequests, Long views) {
        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(categoryMapper.mapToDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .eventDate(event.getEventDate())
                .initiator(userMapper.mapToUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .views(views)
                .build();
    }

    public Event mapToEntity(EventRequestDto dto, Category category, Location location, User initiator) {
        return Event.builder()
                .title(dto.getTitle())
                .annotation(dto.getAnnotation())
                .category(category)
                .description(dto.getDescription())
                .initiator(initiator)
                .eventDate(dto.getEventDate())
                .location(location)
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .build();
    }

    public EventDetailDto mapToDetailDto(Event event, Integer confirmedRequests, Long views) {
        return EventDetailDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(categoryMapper.mapToDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .createdOn(event.getCreateDate())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(userMapper.mapToUserShortDto(event.getInitiator()))
                .location(locationMapper.mapToDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublicationDate())
                .requestModeration(event.getRequestModeration())
                .state(event.getPublicationState())
                .views(views)
                .build();
    }

    public Event mapToEntityForUpdate(Event event, EventRequestDto dto, Category category, Location location,
                                      User initiator) {
        return Event.builder()
                .id(event.getId())
                .title(dto.getTitle() != null ? dto.getTitle() : event.getTitle())
                .annotation(dto.getAnnotation() != null ? dto.getAnnotation() : event.getAnnotation())
                .category(category)
                .description(dto.getDescription() != null ? dto.getDescription() : event.getDescription())
                .initiator(initiator)
                .eventDate(dto.getEventDate() != null ? dto.getEventDate() : event.getEventDate())
                .location(location)
                .paid(dto.getPaid() != null ? dto.getPaid() : event.getPaid())
                .participantLimit(dto.getParticipantLimit() != null ? dto.getParticipantLimit()
                        : event.getParticipantLimit())
                .requestModeration(dto.getRequestModeration() != null ? dto.getRequestModeration()
                        : event.getRequestModeration())
                .publicationState(getPublicationStateByAction(dto.getStateAction(), event.getPublicationState()))
                .build();
    }

    private PublicationState getPublicationStateByAction(PublicationStateAction action, PublicationState existing) {
        if (action == null) {
            return existing;
        }

        return PUBLISH_EVENT.equals(action) ? PUBLISHED : CANCELED;
    }


}
