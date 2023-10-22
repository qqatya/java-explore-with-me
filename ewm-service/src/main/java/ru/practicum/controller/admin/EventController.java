package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventDetailDto;
import ru.practicum.dto.event.EventRequestDto;
import ru.practicum.dto.type.PublicationState;
import ru.practicum.service.EventService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
public class EventController {

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
    public List<EventDetailDto> find(@RequestParam List<Long> users, @RequestParam List<PublicationState> states,
                                     @RequestParam List<Long> categories,
                                     @NotNull @DateTimeFormat(pattern = DATE_TIME_PATTERN)
                                     @RequestParam LocalDateTime rangeStart,
                                     @NotNull @DateTimeFormat(pattern = DATE_TIME_PATTERN)
                                     @RequestParam LocalDateTime rangeEnd,
                                     @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                     @PositiveOrZero @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventService.find(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    /**
     * Обновление события
     *
     * @param eventId Идентификатор события
     * @param dto     Объект, содержащий данные о событии
     * @return Детальная информация о событии
     */
    @PatchMapping("/{eventId}")
    public EventDetailDto update(@PathVariable Long eventId, @RequestBody EventRequestDto dto) {
        return eventService.update(eventId, dto);
    }


}
