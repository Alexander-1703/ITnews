package ru.tinkoff.edu.java.bot.exchandler;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.tinkoff.edu.java.dto.ApiErrorResponse;

@RestControllerAdvice
public class BotErrorControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                "Некорректные параметры запроса",
                "400",
                e.getClass().getSimpleName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
