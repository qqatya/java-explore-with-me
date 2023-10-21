package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.practicum.dto.ErrorInfo;
import ru.practicum.exception.NotFoundException;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class ErrorController extends ResponseEntityExceptionHandler {

    /**
     * Обработчик IllegalArgumentException
     *
     * @param e Эксепшн
     * @return Объект, содержащий сообщение об ошибке
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfo processIllegalArgumentException(IllegalArgumentException e, HttpStatus status) {
        log.debug(e.getMessage());
        return new ErrorInfo(status.name(), status.getReasonPhrase(), e.getMessage(), LocalDateTime.now());
    }

    /**
     * Обработчик NotFoundException
     *
     * @param e Эксепшн
     * @return Объект, содержащий сообщение об ошибке
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfo processNotFoundException(NotFoundException e, HttpStatus status) {
        log.debug(e.getMessage());
        return new ErrorInfo(status.name(), status.getReasonPhrase(), e.getMessage(), LocalDateTime.now());
    }

    /**
     * Обработчик ValidationException
     *
     * @param e Эксепшн
     * @return Объект, содержащий сообщение об ошибке
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfo processValidationException(ValidationException e, HttpStatus status) {
        log.debug(e.getMessage());
        return new ErrorInfo(status.name(), status.getReasonPhrase(), e.getMessage(), LocalDateTime.now());
    }

    /**
     * Обработчик непредвиденных ошибок
     *
     * @param e Эксепшн
     * @return Объект, содержащий сообщение об ошибке
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorInfo processException(Exception e, HttpStatus status) {
        log.error("Unexpected error: ", e);
        return new ErrorInfo(status.name(), status.getReasonPhrase(), e.getMessage(), LocalDateTime.now());
    }

}
