package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class StatsController {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final StatsService statsService;

    /**
     * Сохранение информации о новом запросе
     *
     * @param dto Объект, содержащий данные о запросе
     */
    @PostMapping("/hit")
    @ResponseStatus(CREATED)
    public void create(@RequestBody @Valid HitRequestDto dto) {
        statsService.create(dto);
    }

    /**
     * Получение статистики по посещениям
     *
     * @param start  Дата и время начала диапазона, за который нужно выгрузить статистику
     * @param end    Дата и время конца диапазона, за который нужно выгрузить статистику
     * @param uris   Список uri, для которых нужно выгрузить статистику
     * @param unique Нужно ли учитывать только уникальные посещения
     * @return Список объектов, содержащих данные о статистике
     */
    @GetMapping("/stats")
    public List<StatsDto> getStats(@NotNull @PastOrPresent @DateTimeFormat(pattern = DATE_TIME_PATTERN)
                                   @RequestParam LocalDateTime start,
                                   @NotNull @DateTimeFormat(pattern = DATE_TIME_PATTERN)
                                   @RequestParam LocalDateTime end,
                                   @RequestParam(required = false) List<String> uris,
                                   @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        checkArgument(start.isBefore(end) || start.equals(end),
                "start date should be less than or equal to end date");
        return statsService.getStats(start, end, uris, unique);
    }
}
