package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.common.AddressDto;
import ru.yandex.practicum.model.AddressEmbeddable;

@Component
public class AddressEmbeddableMapper {

    public AddressEmbeddable toEmbeddable(AddressDto dto) {
        if (dto == null) return null;
        return AddressEmbeddable.builder()
                .country(dto.getCountry())
                .city(dto.getCity())
                .street(dto.getStreet())
                .house(dto.getHouse())
                .flat(dto.getFlat())
                .build();
    }

    public AddressDto toDto(AddressEmbeddable embeddable) {
        if (embeddable == null) return null;
        return AddressDto.builder()
                .country(embeddable.getCountry())
                .city(embeddable.getCity())
                .street(embeddable.getStreet())
                .house(embeddable.getHouse())
                .flat(embeddable.getFlat())
                .build();
    }
}
