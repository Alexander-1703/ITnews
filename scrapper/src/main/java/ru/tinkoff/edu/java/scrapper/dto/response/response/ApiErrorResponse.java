package ru.tinkoff.edu.java.scrapper.dto.response.response;

import java.util.Arrays;
import java.util.List;

public record ApiErrorResponse(String description, String code, String exceptionName, String exceptionMessage,
                               List<String> stacktrace) {
    public ApiErrorResponse(String code, String description, Exception ex) {
        this(description,
                code,
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList());
    }
}
