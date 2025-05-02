package ru.yandex.practicum.controller;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.dto.common.ApiError;
import ru.yandex.practicum.exception.NoOrderFoundException;
import ru.yandex.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.mapper.ApiErrorResponseMapper;

@RestControllerAdvice
public class OrderExceptionHandler {

    @ExceptionHandler(NotAuthorizedUserException.class)
    public ResponseEntity<ApiError> handle(NotAuthorizedUserException ex) {
        ApiError error = ApiErrorResponseMapper
                .toApiError(ex, HttpStatus.UNAUTHORIZED, "Пользователь не авторизован");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    public ResponseEntity<ApiError> handle(NoSpecifiedProductInWarehouseException ex) {
        ApiError error = ApiErrorResponseMapper
                .toApiError(ex, HttpStatus.BAD_REQUEST, "Некоторые товары отсутствуют на складе");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(NoOrderFoundException.class)
    public ResponseEntity<ApiError> handle(NoOrderFoundException ex) {
        ApiError error = ApiErrorResponseMapper.toApiError(ex, HttpStatus.BAD_REQUEST, "Заказ не найден");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiError> handle(FeignException ex) {
        ApiError error;

        try {
            error = ApiErrorResponseMapper.fromJson(ex.contentUTF8());
        } catch (Exception ignore) {
            error = ApiErrorResponseMapper.toApiError(ex, HttpStatus.valueOf(ex.status()), "Неизвестная ошибка");
        }

        return ResponseEntity.status(ex.status()).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handle(Exception ex) {
        ApiError error = ApiErrorResponseMapper.toApiError(ex, HttpStatus.INTERNAL_SERVER_ERROR, "Неизвестная ошибка");
        return ResponseEntity.badRequest().body(error);
    }

}
