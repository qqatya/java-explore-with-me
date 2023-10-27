package ru.practicum.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.EventCreateDto;
import ru.practicum.dto.event.EventDetailDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.EventUpdateDto;
import ru.practicum.dto.event.location.LocationDto;
import ru.practicum.dto.type.EventSort;
import ru.practicum.dto.type.PublicationState;
import ru.practicum.entity.*;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.HitRequestMapper;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.EventService;
import ru.practicum.service.LocationService;
import ru.practicum.service.RequestService;
import ru.practicum.service.StatsService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static ru.practicum.dto.type.EventAction.PUBLISH_EVENT;
import static ru.practicum.dto.type.EventAction.REJECT_EVENT;
import static ru.practicum.dto.type.EventSort.EVENT_DATE;
import static ru.practicum.dto.type.PublicationState.PENDING;
import static ru.practicum.dto.type.PublicationState.PUBLISHED;
import static ru.practicum.exception.type.ExceptionType.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final LocationService locationService;

    private final RequestService requestService;

    private final StatsService statsService;

    private final HitRequestMapper hitRequestMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> findByUserId(Long userId, Integer from, Integer size) {
        log.info("searching for events by userId = {}. from = {}, size = {}", userId, from, size);
        int page = from != 0 ? from / size : from;
        Pageable pageable = PageRequest.of(page, size);
        List<Event> events = eventRepository.findByInitiatorId(userId, pageable);

        log.info("found {} events", events.size());
        return mapToShortDtos(events);
    }

    @Override
    public EventDetailDto create(Long userId, EventCreateDto dto) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND.getValue(), userId)));
        Long categoryId = dto.getCategory();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_FOUND.getValue(), categoryId)));
        Location location = locationService.findOrCreateLocation(dto.getLocation());
        Event event = eventRepository.save(eventMapper.mapToEntity(dto, category, location, initiator));

        log.info("created new event: {}", event);
        return mapToDetailDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public EventDetailDto getDetailInfo(Long userId, Long eventId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(String.format(USER_NOT_FOUND.getValue(), userId));
        }
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND.getValue(), eventId)));

        log.info("found event: {}", event);
        return mapToDetailDto(event);
    }

    @Override
    public EventDetailDto updateByUser(Long userId, Long eventId, EventUpdateDto dto) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND.getValue(), userId)));
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND.getValue(), eventId)));

        if (PUBLISHED.equals(event.getPublicationState()) || isEventDateWithinTwoHours(event.getEventDate())) {
            throw new SecurityException(EVENT_UNAVAILABLE_FOR_EDITING.getValue());
        }
        Category category = getCategoryForUpdate(dto.getCategory(), event.getCategory());
        Location location = getLocationForUpdate(dto.getLocation(), event.getLocation());

        Event updated = eventRepository.save(eventMapper
                .mapToEntityForUpdate(event, dto, category, location, initiator));
        log.info("event: {} has been updated", updated);

        return mapToDetailDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDetailDto> findByAdmin(List<Long> userIds, List<PublicationState> states, List<Long> categoryIds,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        log.info("searching for events by userIds = {}, states = {}, categoryIds = {}, rangeStart = {}, rangeEnd = {}, "
                + "from = {}, size = {}", userIds, states, categoryIds, rangeStart, rangeEnd, from, size);
        int page = from != 0 ? from / size : from;
        Pageable pageable = PageRequest.of(page, size);
        BooleanBuilder filters = findFilters(userIds, states, categoryIds, rangeStart, rangeEnd);
        List<Event> events = eventRepository.findAll(filters, pageable).getContent();

        log.info("found {} events", events.size());
        return events.stream().map(this::mapToDetailDto).collect(Collectors.toList());
    }

    @Override
    public EventDetailDto updateByAdmin(Long eventId, EventUpdateDto dto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND.getValue(), eventId)));

        if (REJECT_EVENT.equals(dto.getStateAction()) && PUBLISHED.equals(event.getPublicationState())) {
            throw new IllegalStateException(String.format(EVENT_ALREADY_PUBLISHED.getValue(), eventId));
        }
        if (PUBLISH_EVENT.equals(dto.getStateAction()) && !PENDING.equals(event.getPublicationState())) {
            throw new IllegalStateException(EVENT_MUST_BE_PENDING.getValue());
        }
        LocalDateTime publicationDate = event.getPublicationDate() != null ? event.getPublicationDate()
                : LocalDateTime.now();

        if (!eventDateIsAfterOrEqHourBeforePublicationDate(event.getEventDate(), publicationDate)) {
            throw new IllegalStateException(EVENT_UNAVAILABLE_FOR_EDITING_ADMIN.getValue());
        }
        Category category = getCategoryForUpdate(dto.getCategory(), event.getCategory());
        Location location = getLocationForUpdate(dto.getLocation(), event.getLocation());

        Event updated = eventRepository.save(eventMapper
                .mapToEntityForUpdate(event, dto, category, location, event.getInitiator()));
        log.info("event: {} has been updated", updated);

        return mapToDetailDto(updated);
    }

    @Override
    public List<EventShortDto> mapToShortDtos(List<Event> events) {
        return events.stream()
                .map(e -> {
                    Long views = statsService.getViews(e.getCreateDate(), e.getEventDate(), createUris(e.getId()));

                    return eventMapper.mapToShortDto(e, requestService.findConfirmedRequestsAmount(e.getId()), views);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<EventShortDto> find(String text, List<Long> categoryIds, Boolean paid, LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort, Integer from,
                                    Integer size, HttpServletRequest request) {
        BooleanBuilder filters = findPublicFilters(text, categoryIds, paid, rangeStart, rangeEnd);

        filters.and(QEvent.event.publicationState.eq(PUBLISHED));
        log.info("performing public events search by text = {}, categoryIds = {}, paid = {}, rangeStart = {}, "
                        + "rangeEnd = {}, onlyAvailable = {}, sort = {}, from = {},  size = {}", text, categoryIds,
                paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        int page = from != 0 ? from / size : from;
        Pageable pageable = PageRequest.of(page, size);
        List<Event> events = eventRepository.findAll(filters, pageable).getContent();

        if (onlyAvailable) {
            events = events.stream()
                    .filter(e -> e.getParticipantLimit() < requestService.findConfirmedRequestsAmount(e.getId()))
                    .collect(Collectors.toList());
        }
        List<EventShortDto> shortDtos = mapToShortDtos(events);

        statsService.create(hitRequestMapper.mapToDto(request.getRequestURI(), request.getRemoteAddr()));
        if (sort != null) {
            return shortDtos.stream()
                    .sorted(findComparator(sort))
                    .collect(Collectors.toList());
        }
        return shortDtos;
    }

    @Override
    public EventDetailDto findPublishedById(Long id, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndPublicationStateEquals(id, PUBLISHED)
                .orElseThrow(() -> new NoSuchElementException(String.format(EVENT_NOT_FOUND.getValue(), id)));
        log.info("found event: {}", event);
        EventDetailDto dto = mapToDetailDto(event);

        statsService.create(hitRequestMapper.mapToDto(request.getRequestURI(), request.getRemoteAddr()));
        return dto;
    }

    private boolean isEventDateWithinTwoHours(LocalDateTime eventDate) {
        LocalDateTime withinTwoHours = LocalDateTime.now().plusHours(2);

        return withinTwoHours.equals(eventDate) || withinTwoHours.isAfter(eventDate);
    }

    private Category getCategoryForUpdate(Long categoryId, Category existing) {
        return categoryId != null ? categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_FOUND.getValue(), categoryId)))
                : existing;
    }

    private Location getLocationForUpdate(LocationDto dto, Location existing) {
        return dto != null ? locationService.findOrCreateLocation(dto)
                : existing;
    }

    private EventDetailDto mapToDetailDto(Event event) {
        Long views = statsService.getViews(event.getCreateDate(), event.getEventDate(), createUris(event.getId()));

        return eventMapper.mapToDetailDto(event, requestService.findConfirmedRequestsAmount(event.getId()), views);
    }

    private List<String> createUris(Long eventId) {
        return List.of("/events/" + eventId);
    }

    private boolean eventDateIsAfterOrEqHourBeforePublicationDate(LocalDateTime eventDate,
                                                                  LocalDateTime publicationDate) {
        LocalDateTime plusHour = publicationDate.plusHours(1);
        return eventDate.equals(plusHour) || eventDate.isAfter(plusHour);
    }

    private BooleanBuilder findFilters(List<Long> userIds, List<PublicationState> states, List<Long> categoryIds,
                                       LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        BooleanBuilder filters = new BooleanBuilder();

        if (userIds != null && !userIds.isEmpty()) {
            BooleanExpression userIdsIn = QEvent.event.initiator.id.in(userIds);

            filters.and(userIdsIn);
        }
        if (states != null && !states.isEmpty()) {
            BooleanExpression publicationStatesIn = QEvent.event.publicationState.in(states);

            filters.and(publicationStatesIn);
        }
        if (categoryIds != null && !categoryIds.isEmpty()) {
            BooleanExpression categoryIdsIn = QEvent.event.category.id.in(categoryIds);

            filters.and(categoryIdsIn);
        }
        if (rangeStart != null) {
            BooleanExpression eventDateAfterOrEq = QEvent.event.eventDate.goe(rangeStart);

            filters.and(eventDateAfterOrEq);
        }
        if (rangeEnd != null) {
            BooleanExpression eventDateBeforeOrEq = QEvent.event.eventDate.loe(rangeEnd);

            filters.and(eventDateBeforeOrEq);
        }
        return filters;
    }

    private BooleanBuilder findPublicFilters(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd) {
        BooleanBuilder filters = new BooleanBuilder();

        if (text != null && !text.isBlank()) {
            BooleanExpression annotationEq = QEvent.event.annotation.likeIgnoreCase(text);
            BooleanExpression descriptionEq = QEvent.event.description.likeIgnoreCase(text);

            filters.and(annotationEq);
            filters.or(descriptionEq);
        }
        if (categories != null && !categories.isEmpty()) {
            BooleanExpression categoriesIn = QEvent.event.category.id.in(categories);

            filters.and(categoriesIn);
        }
        if (paid != null) {
            BooleanExpression paidEq = QEvent.event.paid.eq(paid);

            filters.and(paidEq);
        }
        if (rangeStart != null) {
            BooleanExpression eventDateAfterOrEq = QEvent.event.eventDate.goe(rangeStart);

            filters.and(eventDateAfterOrEq);
        }
        if (rangeEnd != null) {
            BooleanExpression eventDateBeforeOrEq = QEvent.event.eventDate.loe(rangeEnd);

            filters.and(eventDateBeforeOrEq);
        }
        if (rangeStart == null && rangeEnd == null) {
            BooleanExpression eventDateAfterNow = QEvent.event.eventDate.gt(LocalDateTime.now());

            filters.and(eventDateAfterNow);
        }
        return filters;
    }

    private Comparator<EventShortDto> findComparator(EventSort sort) {
        return EVENT_DATE.equals(sort)
                ? Comparator.comparing(EventShortDto::getEventDate).reversed()
                : Comparator.comparing(EventShortDto::getViews).reversed();
    }

}
