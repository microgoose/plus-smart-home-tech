package ru.yandex.practicum.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.client.DeliveryClient;
import ru.yandex.practicum.client.PaymentClient;
import ru.yandex.practicum.client.ShoppingCartClient;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.common.AddressDto;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.order.ProductReturnRequest;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.order.OrderState;
import ru.yandex.practicum.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final WarehouseClient warehouseClient;
    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;
    private final ShoppingCartClient shoppingCartClient;

    private final OrderRepository repository;
    private final OrderMapper mapper;

    @Override
    public List<OrderDto> getClientOrders(String username) {
        return mapper.toDto(repository.findByUsername(username));
    }

    @Override
    @Transactional
    public OrderDto createNewOrder(CreateNewOrderRequest request) {
        // Получить корзину
        ShoppingCartDto cart = shoppingCartClient.getCart(request.getShoppingCart().getShoppingCartId());

        // Проверка доступности на складе
        BookedProductsDto booking = warehouseClient.checkProductAvailability(cart);

        // Создание нового заказа
        Order order = Order.builder()
                .orderId(UUID.randomUUID())
                .username(cart.getUsername())
                .shoppingCartId(cart.getShoppingCartId())
                .products(cart.getProducts())
                .deliveryWeight(booking.getDeliveryWeight())
                .deliveryVolume(booking.getDeliveryVolume())
                .fragile(booking.isFragile())
                .state(OrderState.NEW)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Планируем доставку
        AddressDto fromAddress = warehouseClient.getWarehouseAddress();
        DeliveryDto deliveryDto = deliveryClient.planDelivery(DeliveryDto.builder()
                .orderId(order.getOrderId())
                .fromAddress(fromAddress)
                .toAddress(request.getDeliveryAddress())
                .build());
        order.setDeliveryId(deliveryDto.getDeliveryId());

        return mapper.toDto(repository.save(order));
    }

    @Override
    @Transactional
    public OrderDto calculateDeliveryCost(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        Double cost = deliveryClient.deliveryCost(mapper.toDto(order));
        order.setDeliveryPrice(cost);
        order.setUpdatedAt(LocalDateTime.now());
        return mapper.toDto(repository.save(order));
    }

    @Override
    @Transactional
    public OrderDto calculateTotalCost(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        Double total = paymentClient.getTotalCost(mapper.toDto(order));
        order.setTotalPrice(total);
        order.setUpdatedAt(LocalDateTime.now());
        return mapper.toDto(repository.save(order));
    }

    @Override
    @Transactional
    public OrderDto assembleOrder(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        warehouseClient.assemblyProductsForOrder(mapper.toAssemblyRequest(order));
        order.setState(OrderState.ASSEMBLED);
        order.setUpdatedAt(LocalDateTime.now());
        return mapper.toDto(repository.save(order));
    }

    @Override
    @Transactional
    public OrderDto processPayment(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        if (order.getState() != OrderState.ASSEMBLED)
            throw new IllegalStateException("Order must be ASSEMBLED before payment");

        PaymentDto payment = paymentClient.payment(mapper.toDto(order));

        order.setPaymentId(payment.getPaymentId());
        order.setProductPrice(payment.getTotalPayment() - payment.getDeliveryTotal() - payment.getFeeTotal());
        order.setDeliveryPrice(payment.getDeliveryTotal());
        order.setTotalPrice(payment.getTotalPayment());
        order.setState(OrderState.PAID);
        order.setUpdatedAt(LocalDateTime.now());

        return mapper.toDto(repository.save(order));
    }

    @Override
    @Transactional
    public OrderDto planDelivery(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        order.setState(OrderState.ON_DELIVERY);
        order.setUpdatedAt(LocalDateTime.now());
        return mapper.toDto(repository.save(order));
    }

    @Override
    @Transactional
    public OrderDto deliverOrder(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        deliveryClient.deliverySuccessful(orderId);
        order.setState(OrderState.DELIVERED);
        order.setUpdatedAt(LocalDateTime.now());
        return mapper.toDto(repository.save(order));
    }

    @Override
    @Transactional
    public OrderDto completeOrder(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        if (order.getState() != OrderState.DELIVERED)
            throw new IllegalStateException("Only delivered orders can be completed");
        order.setState(OrderState.COMPLETED);
        order.setUpdatedAt(LocalDateTime.now());
        return mapper.toDto(repository.save(order));
    }

    @Override
    @Transactional
    public OrderDto productReturn(ProductReturnRequest request) {
        Order order = getOrderOrThrow(request.getOrderId());

        if (Set.of(OrderState.CANCELED, OrderState.PRODUCT_RETURNED,
                OrderState.DELIVERY_FAILED, OrderState.PAYMENT_FAILED,
                OrderState.ASSEMBLY_FAILED).contains(order.getState())) {
            throw new IllegalStateException("Cannot return order in state: " + order.getState());
        }

        warehouseClient.acceptReturn(request.getProducts());
        order.setState(OrderState.PRODUCT_RETURNED);
        order.setUpdatedAt(LocalDateTime.now());
        return mapper.toDto(repository.save(order));
    }

    @Override
    @Transactional
    public OrderDto handlePaymentFailure(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        paymentClient.paymentFailed(order.getPaymentId());
        order.setState(OrderState.PAYMENT_FAILED);
        order.setUpdatedAt(LocalDateTime.now());
        return mapper.toDto(repository.save(order));
    }

    @Override
    @Transactional
    public OrderDto handleAssemblyFailure(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        order.setState(OrderState.ASSEMBLY_FAILED);
        order.setUpdatedAt(LocalDateTime.now());
        return mapper.toDto(repository.save(order));
    }

    @Override
    @Transactional
    public OrderDto handleDeliveryFailure(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        deliveryClient.deliveryFailed(orderId);
        order.setState(OrderState.DELIVERY_FAILED);
        order.setUpdatedAt(LocalDateTime.now());
        return mapper.toDto(repository.save(order));
    }

    private Order getOrderOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + id));
    }
}
