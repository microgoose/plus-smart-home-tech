package ru.yandex.practicum.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.model.delivery.DeliveryStatus;

@Component
@RequiredArgsConstructor
public class DeliveryMapper {

    private final AddressEmbeddableMapper addressEmbeddableMapper;

    public Delivery toEntity(DeliveryDto dto) {
        return Delivery.builder()
                .deliveryId(dto.getDeliveryId())
                .orderId(dto.getOrderId())
                .fromAddress(addressEmbeddableMapper.toEmbeddable(dto.getFromAddress()))
                .toAddress(addressEmbeddableMapper.toEmbeddable(dto.getToAddress()))
                .deliveryState(dto.getDeliveryState() == null?
                        DeliveryStatus.CREATED : DeliveryStatus.valueOf(dto.getDeliveryState()))
                .build();
    }

    public DeliveryDto toDto(Delivery delivery) {
        return DeliveryDto.builder()
                .deliveryId(delivery.getDeliveryId())
                .orderId(delivery.getOrderId())
                .fromAddress(addressEmbeddableMapper.toDto(delivery.getFromAddress()))
                .toAddress(addressEmbeddableMapper.toDto(delivery.getToAddress()))
                .deliveryState(delivery.getDeliveryState().name())
                .build();
    }
}
