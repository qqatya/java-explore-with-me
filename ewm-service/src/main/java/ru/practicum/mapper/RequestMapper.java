package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.event.request.RequestDto;
import ru.practicum.dto.event.request.RequestStatusDto;
import ru.practicum.entity.Request;

import java.util.List;
import java.util.stream.Collectors;

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
}
