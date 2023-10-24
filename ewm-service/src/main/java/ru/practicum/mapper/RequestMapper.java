package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.event.request.RequestDto;
import ru.practicum.dto.event.request.RequestStatusDto;
import ru.practicum.dto.type.RequestStatus;
import ru.practicum.entity.Event;
import ru.practicum.entity.Request;
import ru.practicum.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.dto.type.RequestStatus.CONFIRMED;
import static ru.practicum.dto.type.RequestStatus.PENDING;

@Component
public class RequestMapper {

    public List<RequestDto> mapToDtos(List<Request> requests) {
        return requests.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public RequestDto mapToDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .created(request.getCreateDate())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    public RequestStatusDto mapToStatusDto(List<Request> confirmed, List<Request> rejected) {
        return RequestStatusDto.builder()
                .confirmedRequests(mapToDtos(confirmed))
                .rejectedRequests(mapToDtos(rejected))
                .build();
    }

    public Request mapToEntity(User requester, Event event, boolean isPreModerated) {
        RequestStatus status = event.getParticipantLimit() == 0 && isPreModerated ? PENDING : isPreModerated ? PENDING
                : CONFIRMED;

        return Request.builder()
                .requester(requester)
                .event(event)
                .status(status)
                .createDate(LocalDateTime.now())
                .build();
    }
}
