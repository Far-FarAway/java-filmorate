package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "ru.yandex.practicum.filmorate")
public class ErrorHandler {
    @ExceptionHandler
    public ErrorResponse handleNotFoundException(final NotFoundException ex) {
        return new ErrorResponse("Данные не найдены: " + ex.getMessage());
    }

    @ExceptionHandler
    public ErrorResponse handleValidException(final ConditionNotMetException ex) {
        return new ErrorResponse("Валидация данных не прошла: " + ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpectedException(final Throwable ex) {
        return new ErrorResponse("Произошла непредвиденная ошибка: " + ex.getMessage());
    }

}