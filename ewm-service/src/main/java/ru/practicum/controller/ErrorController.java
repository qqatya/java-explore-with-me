package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.dto.ErrorInfo;
import ru.practicum.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class ErrorController {

    /**
     * Обработчик SecurityException
     *
     * @param e Эксепшн
     * @return Объект, содержащий сообщение об ошибке
     */
    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(CONFLICT)
    public ErrorInfo processSecurityException(SecurityException e) {
        log.debug(e.getMessage());
        return new ErrorInfo(CONFLICT.name(), CONFLICT.getReasonPhrase(), e.getMessage(), LocalDateTime.now());
    }

    /**
     * Обработчик IllegalStateException
     *
     * @param e Эксепшн
     * @return Объект, содержащий сообщение об ошибке
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(CONFLICT)
    public ErrorInfo processIllegalStateException(IllegalStateException e) {
        log.debug(e.getMessage());
        return new ErrorInfo(CONFLICT.name(), CONFLICT.getReasonPhrase(), e.getMessage(), LocalDateTime.now());
    }

    /**
     * Обработчик эксепшнов
     *
     * @param e Эксепшн
     * @return Объект, содержащий сообщение об ошибке
     */
    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class, IllegalArgumentException.class,
            HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class, NotFoundException.class})
    @ResponseStatus(code = BAD_REQUEST)
    public ErrorInfo handleBadRequestException(Exception e) {
        return new ErrorInfo(BAD_REQUEST.name(), BAD_REQUEST.getReasonPhrase(), e.getMessage(), LocalDateTime.now());
    }

    /**
     * Обработчик эксепшнов
     *
     * @param e Эксепшн
     * @return Объект, содержащий сообщение об ошибке
     */
    @ExceptionHandler({ConstraintViolationException.class, DataIntegrityViolationException.class})
    @ResponseStatus(code = CONFLICT)
    public ErrorInfo handleConflictException(Exception e) {
        return new ErrorInfo(CONFLICT.name(), CONFLICT.getReasonPhrase(), e.getMessage(), LocalDateTime.now());
    }

    /**
     * Обработчик NoSuchElementException
     *
     * @param e Эксепшн
     * @return Объект, содержащий сообщение об ошибке
     */
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(code = NOT_FOUND)
    public ErrorInfo handleNoSuchElementException(NoSuchElementException e) {
        return new ErrorInfo(NOT_FOUND.name(), NOT_FOUND.getReasonPhrase(), e.getMessage(), LocalDateTime.now());
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
