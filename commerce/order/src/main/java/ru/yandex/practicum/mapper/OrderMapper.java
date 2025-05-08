package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.order.OrderState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public AssemblyProductsForOrderRequest toAssemblyRequest(Order order) {
        return AssemblyProductsForOrderRequest.builder()
                .orderId(order.getOrderId())
                .productsQuantity(order.getProductsQuantity())
                .build();
    }

    public Order toEntity(OrderDto dto) {
        return Order.builder()
                .orderId(dto.getOrderId())
                .shoppingCartId(dto.getShoppingCartId())
                .paymentId(dto.getPaymentId())
                .deliveryId(dto.getDeliveryId())
                .state(dto.getState())
                .productsQuantity(dto.getProductsQuantity())
                .deliveryWeight(dto.getDeliveryWeight() != null ? dto.getDeliveryWeight() : 0.0)
                .deliveryVolume(dto.getDeliveryVolume() != null ? dto.getDeliveryVolume() : 0.0)
                .fragile(dto.getFragile() != null && dto.getFragile())
                .totalPrice(dto.getTotalPrice() != null ? dto.getTotalPrice() : 0.0)
                .productPrice(dto.getProductPrice() != null ? dto.getProductPrice() : 0.0)
                .deliveryPrice(dto.getDeliveryPrice() != null ? dto.getDeliveryPrice() : 0.0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public OrderDto toDto(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .shoppingCartId(order.getShoppingCartId())
                .productsQuantity(order.getProductsQuantity())
                .paymentId(order.getPaymentId())
                .deliveryId(order.getDeliveryId())
                .state(order.getState())
                .deliveryWeight(order.getDeliveryWeight())
                .deliveryVolume(order.getDeliveryVolume())
                .fragile(order.isFragile())
                .totalPrice(order.getTotalPrice())
                .productPrice(order.getProductPrice())
                .deliveryPrice(order.getDeliveryPrice())
                .build();
    }

    public List<OrderDto> toDto(List<Order> orders) {
        return orders.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public OrderDto fromRequest(CreateNewOrderRequest request) {
        return OrderDto.builder()
                .orderId(null) // ещё не создан
                .shoppingCartId(request.getShoppingCart().getShoppingCartId())
                .productsQuantity(request.getShoppingCart().getProductsQuantity())
                .state(OrderState.NEW)
                .build();
    }
}
