package ru.yandex.practicum.exception;

public class AvroDeserializationException extends RuntimeException {
    public AvroDeserializationException(String message) {
        super(message);
    }
}
