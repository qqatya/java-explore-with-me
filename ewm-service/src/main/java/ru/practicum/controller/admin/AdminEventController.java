package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventDetailDto;
import ru.practicum.dto.event.EventUpdateDto;
import ru.practicum.dto.type.PublicationState;
import ru.practicum.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
public class AdminEventController {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final EventService eventService;

    /**
     * Поиск событий
     *
     * @param users      Идентификаторы инициаторов событий
     * @param states     Состояния публикации событий
     * @param categories Идентификаторы категорий событий
     * @param rangeStart Дата и время, не раньше которых должно произойти событие
     * @param rangeEnd   Дата и время, не позже которых должно произойти событие
     * @param from       Начальный элемент
     * @param size       Количество отображаемых элементов
     * @return Список событий
     */
    @GetMapping
    public List<EventDetailDto> find(@RequestParam(required = false) List<Long> users,
                                     @RequestParam(required = false) List<PublicationState> states,
                                     @RequestParam(required = false) List<Long> categories,
                                     @DateTimeFormat(pattern = DATE_TIME_PATTERN)
                                     @RequestParam(required = false) LocalDateTime rangeStart,
                                     @DateTimeFormat(pattern = DATE_TIME_PATTERN)
                                     @RequestParam(required = false) LocalDateTime rangeEnd,
                                     @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                     @PositiveOrZero @RequestParam(required = false, defaultValue = "10") Integer size) {
        if (rangeStart != null && rangeEnd != null) {
            checkArgument(rangeStart.isBefore(rangeEnd));
        }
        return eventService.findByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    /**
     * Обновление события
     *
     * @param eventId Идентификатор события
     * @param dto     Объект, содержащий данные о событии
     * @return Детальная информация о событии
     */
    @PatchMapping("/{eventId}")
    public EventDetailDto update(@PathVariable Long eventId, @Valid @RequestBody EventUpdateDto dto) {
        return eventService.updateByAdmin(eventId, dto);
    }

}
