package ru.yandex.practicum.telemetry.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.telemetry.dto.ApiError;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ApiError> handlerOtherException(Exception e) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, "Got 500 status Internal server error");
    }

    private ResponseEntity<ApiError> buildErrorResponse(Exception e, HttpStatus status, String message) {
        StackTraceElement sElem = e.getStackTrace()[0];
        String className = sElem.getClassName();
        String str = className.contains(".") ? className.substring(className.lastIndexOf(".") + 1) : className;
        log.error("\n{} error - Class: {}; Method: {}; Line: {}; \nMessage: {}",
                status, str, sElem.getMethodName(), sElem.getLineNumber(), e.getMessage());

        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String statusStr = status.value() + " " + status.getReasonPhrase().replace(" ", "_");

        return ResponseEntity.status(status)
                .body(new ApiError(statusStr, message, e));
    }
}