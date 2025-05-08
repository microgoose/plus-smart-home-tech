package ru.yandex.practicum.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private String httpStatus;             // Пример: "404 NOT_FOUND"
    private String message;                // Техническое сообщение
    private String userMessage;            // Сообщение для пользователя
    private String localizedMessage;       // Локализованное сообщение (если отличается)
    private ApiError cause;               // Первопричина (можно заменить на кастомный тип)
    private List<StackTraceElement> stackTrace; // Трассировка стека
    private List<ApiError> suppressed;     // Подавленные исключения

}
