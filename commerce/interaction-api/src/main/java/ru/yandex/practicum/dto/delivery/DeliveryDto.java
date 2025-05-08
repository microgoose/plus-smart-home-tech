package ru.yandex.practicum.dto.delivery;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.dto.common.AddressDto;

import java.util.UUID;

@Data
@Builder
public class DeliveryDto {
    private UUID deliveryId;
    private AddressDto fromAddress;
    private AddressDto toAddress;
    private UUID orderId;
    private String deliveryState;
}