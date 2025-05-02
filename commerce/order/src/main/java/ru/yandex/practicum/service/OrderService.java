package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.order.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<OrderDto> getClientOrders(String username);

    OrderDto createNewOrder(CreateNewOrderRequest request);

    OrderDto productReturn(ProductReturnRequest request);

    OrderDto processPayment(UUID orderId);

    OrderDto handlePaymentFailure(UUID orderId);

    OrderDto deliverOrder(UUID orderId);

    OrderDto handleDeliveryFailure(UUID orderId);

    OrderDto completeOrder(UUID orderId);

    OrderDto calculateTotalCost(UUID orderId);

    OrderDto calculateDeliveryCost(UUID orderId);

    OrderDto assembleOrder(UUID orderId);

    OrderDto handleAssemblyFailure(UUID orderId);

    OrderDto planDelivery(UUID orderId);
}
