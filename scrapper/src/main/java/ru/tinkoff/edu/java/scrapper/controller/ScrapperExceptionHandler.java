package ru.tinkoff.edu.java.scrapper.controller;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.tinkoff.edu.java.dto.response.ApiErrorResponse;

@RestControllerAdvice
public class ScrapperExceptionHandler {
    private static final String BAD_REQUEST = "400";
    private static final String NOT_FOUND = "404";

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse("Некорректные параметры запроса", BAD_REQUEST, e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse("Ссылка не найдена", NOT_FOUND, e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

}
