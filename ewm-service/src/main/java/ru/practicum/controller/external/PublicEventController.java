package ru.practicum.controller.external;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventDetailDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.type.EventSort;
import ru.practicum.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@RestController
@RequestMapping("/events")
@Validated
@RequiredArgsConstructor
public class PublicEventController {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final EventService eventService;

    /**
     * Поиск событий с возможностью фильтрации
     *
     * @param categories    Идентификаторы категорий событий
     * @param rangeStart    Дата и время, не раньше которых должно произойти событие
     * @param rangeEnd      Дата и время, не позже которых должно произойти событие
     * @param from          Начальный элемент
     * @param size          Количество отображаемых элементов
     * @param text          Текст для поиска в содержимом аннотации и подробном описании события
     * @param paid          Поиск только платных/бесплатных событий
     * @param onlyAvailable Признак поиска событий с неисчерпанным лимитом запросов на участие
     * @param sort          Параметр сортировки
     * @param request       Данные о запросе
     * @return Список событий
     */
    @GetMapping
    public List<EventShortDto> find(@RequestParam(required = false) String text,
                                    @RequestParam(required = false) List<Long> categories,
                                    @RequestParam(required = false) Boolean paid,
                                    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
                                    @RequestParam(required = false) LocalDateTime rangeStart,
                                    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
                                    @RequestParam(required = false) LocalDateTime rangeEnd,
                                    @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                    @RequestParam(required = false) EventSort sort,
                                    @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                    @PositiveOrZero @RequestParam(required = false, defaultValue = "10") Integer size,
                                    HttpServletRequest request) {
        if (rangeStart != null && rangeEnd != null) {
            checkArgument(rangeStart.isBefore(rangeEnd));
        }
        return eventService.find(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size,
                request);
    }

    /**
     * Поиск события по идентификатору
     *
     * @param id      Идентификатор события
     * @param request Данные о запросе
     * @return Событие
     */
    @GetMapping("/{id}")
    public EventDetailDto findById(@PathVariable Long id, HttpServletRequest request) {
        return eventService.findPublishedById(id, request);
    }

}
