package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.request.RequestDto;
import ru.practicum.dto.event.request.RequestStatusDto;
import ru.practicum.dto.event.request.RequestStatusUpdateDto;
import ru.practicum.dto.type.RequestStatus;
import ru.practicum.entity.Event;
import ru.practicum.entity.Request;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.RequestService;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.dto.type.RequestStatus.*;
import static ru.practicum.exception.type.ExceptionType.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final RequestMapper requestMapper;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getRequests(Long userId, Long eventId) {
        List<Request> requests = requestRepository.findByRequesterIdAndEventId(userId, eventId);

        log.info("found {} requests by eventId = {}", requests.size(), eventId);
        return requestMapper.mapToDtos(requests);
    }

    @Override
    public RequestStatusDto changeStatus(Long userId, Long eventId, RequestStatusUpdateDto dto) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(String.format(USER_NOT_FOUND.getValue(), userId));
        }
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND.getValue(), eventId)));

        checkParticipantLimit(event.getParticipantLimit(), eventId);
        Integer requestCount = event.getParticipantLimit();

        for (Long requestId : dto.getRequestIds()) {
            Request request = requestRepository.findById(requestId)
                    .orElseThrow(() -> new NotFoundException(String.format(REQUEST_NOT_FOUND.getValue(), requestId)));

            checkStatus(request.getStatus(), requestId);
            if (requestCount < event.getParticipantLimit()) {
                request.setStatus(dto.getStatus());
                requestCount++;
            } else {
                requestRepository.setRejectedStatusByEventId(eventId);
                break;
            }
        }
        return fillDtoFields(userId, eventId);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer findConfirmedRequestsAmount(Long eventId) {
        return requestRepository.findByEventIdAndStatusEquals(eventId, CONFIRMED).size();
    }

    private RequestStatusDto fillDtoFields(Long userId, Long eventId) {
        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();

        requestRepository.findByRequesterIdAndEventId(userId, eventId).forEach(r -> {
            if (CONFIRMED.equals(r.getStatus())) {
                confirmed.add(r);
            } else if (REJECTED.equals(r.getStatus())) {
                rejected.add(r);
            }
        });
        log.info("confirmed requests size = {}, rejected requests size = {}", confirmed.size(), rejected.size());
        return requestMapper.mapToStatusDto(confirmed, rejected);
    }

    private void checkParticipantLimit(Integer participantLimit, Long eventId) {
        if (participantLimit == 0 || participantLimit.equals(findConfirmedRequestsAmount(eventId))) {
            throw new SecurityException(REACHED_PARTICIPANT_LIMIT.getValue());
        }
    }

    private void checkStatus(RequestStatus status, Long requestId) {
        if (!PENDING.equals(status)) {
            throw new SecurityException(String.format(REQUEST_STATUS_NOT_PENDING.getValue(), requestId));
        }
    }
}