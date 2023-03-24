package ru.tinkoff.edu.java.scrapper.exchandler;

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.tinkoff.edu.java.dto.ApiErrorResponse;

@RestControllerAdvice
public class ScrapperErrorControllerAdvice {
    private static final String BAD_REQUEST = "400";

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse("Некорректные параметры запроса", BAD_REQUEST, e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse("Ссылка не найдена", BAD_REQUEST, e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

}
