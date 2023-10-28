package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.dto.ErrorInfo;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
@Slf4j
public class ErrorController {

    /**
     * Обработчик IllegalArgumentException
     *
     * @param e Эксепшн
     * @return Объект, содержащий сообщение об ошибке
     */
    @ExceptionHandler({IllegalArgumentException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorInfo processSecurityException(IllegalArgumentException e) {
        log.debug(e.getMessage());
        return new ErrorInfo(BAD_REQUEST.name(), BAD_REQUEST.getReasonPhrase(), e.getMessage(), LocalDateTime.now());
    }

    /**
     * Обработчик непредвиденных ошибок
     *
     * @param e Эксепшн
     * @return Объект, содержащий сообщение об ошибке
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorInfo processException(Exception e) {
        log.error("Unexpected error: ", e);
        return new ErrorInfo(INTERNAL_SERVER_ERROR.name(), INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage(),
                LocalDateTime.now());
    }

}
