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
import ru.practicum.entity.User;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.RequestService;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static ru.practicum.dto.type.PublicationState.PUBLISHED;
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
    public List<RequestDto> findRequests(Long userId, Long eventId) {
        List<Request> requests = requestRepository.findByEventInitiatorIdAndEventId(userId, eventId);

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

        if (event.getRequestModeration()) {
            checkParticipantLimitForUpdate(event.getParticipantLimit(), eventId);
        }
        Integer requestCount = event.getParticipantLimit();
        List<Request> requests = requestRepository.findByIdIn(dto.getRequestIds());

        for (Request request : requests) {
            checkStatus(request.getStatus(), request.getId());
            if (requestCount <= event.getParticipantLimit()) {
                request.setStatus(dto.getStatus());
                requestRepository.save(request);
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

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> findRequests(Long userId) {
        List<Request> requests = requestRepository.findByRequesterId(userId);

        log.info("found {} requests of userId = {}", requests.size(), userId);
        return requestMapper.mapToDtos(requests);
    }

    @Override
    public RequestDto create(Long requesterId, Long eventId) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND.getValue(), requesterId)));
        if (requestRepository.findByRequesterIdAndEventId(requesterId, eventId).isPresent()) {
            throw new IllegalStateException(String.format(REQUEST_DUPLICATE.getValue(), requesterId, eventId));
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND.getValue(), eventId)));

        if (requesterId.equals(event.getInitiator().getId())) {
            throw new IllegalStateException(EVENT_INITIATED_BY_REQUESTER.getValue());
        }
        checkParticipantLimitForCreation(event.getParticipantLimit(), eventId, event.getRequestModeration());

        if (!PUBLISHED.equals(event.getPublicationState())) {
            throw new SecurityException(REQUEST_FOR_UNPUBLISHED_EVENT.getValue());
        }
        Request request = requestRepository.save(requestMapper.mapToEntity(requester, event,
                event.getRequestModeration()));

        log.info("created new request: {}", request);
        return requestMapper.mapToDto(request);
    }

    @Override
    public RequestDto cancel(Long requesterId, Long requestId) {
        if (userRepository.findById(requesterId).isEmpty()) {
            throw new NotFoundException(String.format(USER_NOT_FOUND.getValue(), requesterId));
        }
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format(REQUEST_NOT_FOUND.getValue(), requestId)));

        checkArgument(!CANCELED.equals(request.getStatus()),
                String.format(REQUEST_ALREADY_CANCELED.getValue(), requestId));
        request.setStatus(CANCELED);
        Request canceled = requestRepository.save(request);

        log.info("requestId = {} has been canceled", requestId);
        return requestMapper.mapToDto(canceled);
    }

    private RequestStatusDto fillDtoFields(Long userId, Long eventId) {
        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();

        requestRepository.findByEventInitiatorIdAndEventId(userId, eventId).forEach(r -> {
            if (CONFIRMED.equals(r.getStatus())) {
                confirmed.add(r);
            } else if (REJECTED.equals(r.getStatus())) {
                rejected.add(r);
            }
        });
        log.info("confirmed requests size = {}, rejected requests size = {}", confirmed.size(), rejected.size());
        return requestMapper.mapToStatusDto(confirmed, rejected);
    }

    private void checkParticipantLimitForUpdate(Integer participantLimit, Long eventId) {
        Integer confirmedReqs = findConfirmedRequestsAmount(eventId);

        if (participantLimit <= confirmedReqs) {
            throw new SecurityException(REACHED_PARTICIPANT_LIMIT.getValue());
        }
    }

    private void checkParticipantLimitForCreation(Integer participantLimit, Long eventId, Boolean isPreModerated) {
        Integer confirmedReqs = findConfirmedRequestsAmount(eventId);

        if (!isPreModerated && participantLimit.equals(confirmedReqs)) {
            throw new SecurityException(REACHED_PARTICIPANT_LIMIT.getValue());
        }
        if (isPreModerated && participantLimit != 0 && participantLimit.equals(confirmedReqs)) {
            throw new SecurityException(REACHED_PARTICIPANT_LIMIT.getValue());
        }
    }

    private void checkStatus(RequestStatus status, Long requestId) {
        if (!PENDING.equals(status)) {
            throw new SecurityException(String.format(REQUEST_STATUS_NOT_PENDING.getValue(), requestId));
        }
    }
}