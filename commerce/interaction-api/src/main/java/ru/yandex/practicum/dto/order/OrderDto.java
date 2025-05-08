package ru.yandex.practicum.dto.order;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.model.order.OrderState;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class OrderDto {

    private UUID orderId;
    private UUID shoppingCartId;
    private Map<UUID, Long> productsQuantity;
    private UUID paymentId;
    private UUID deliveryId;

    private OrderState state;

    private Double deliveryWeight;
    private Double deliveryVolume;
    private Boolean fragile;

    private Double totalPrice;
    private Double deliveryPrice;
    private Double productPrice;
}