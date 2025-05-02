package ru.yandex.practicum.dto.common;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pageable {
    private int page;
    private int size;
    private List<String> sort;
}