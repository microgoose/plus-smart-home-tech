package ru.yandex.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoOrderFoundException extends RuntimeException {

    public NoOrderFoundException(String message) {
        super(message);
    }

    public NoOrderFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
