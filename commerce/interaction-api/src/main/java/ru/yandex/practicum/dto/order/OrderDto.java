package ru.yandex.practicum.dto.order;

import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class OrderDto {

    private UUID orderId;
    private UUID shoppingCartId;
    private Map<UUID, Long> products; // <productId, quantity>
    private UUID paymentId;
    private UUID deliveryId;

    private String state; // Возможные значения: NEW, ON_PAYMENT, ON_DELIVERY, DONE, и др.

    private Double deliveryWeight;
    private Double deliveryVolume;
    private Boolean fragile;

    private Double totalPrice;
    private Double deliveryPrice;
    private Double productPrice;
}