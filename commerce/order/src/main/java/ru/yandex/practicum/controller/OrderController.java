package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.order.ProductReturnRequest;
import ru.yandex.practicum.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getClientOrders(@RequestParam String username) {
        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(orderService.getClientOrders(username));
    }

    @PutMapping
    public ResponseEntity<OrderDto> createNewOrder(@RequestBody CreateNewOrderRequest request) {
        return ResponseEntity.ok(orderService.createNewOrder(request));
    }

    @PostMapping("/return")
    public ResponseEntity<OrderDto> productReturn(@RequestBody ProductReturnRequest request) {
        return ResponseEntity.ok(orderService.productReturn(request));
    }

    @PostMapping("/payment")
    public ResponseEntity<OrderDto> payment(@RequestBody UUID orderId) {
        return ResponseEntity.ok(orderService.processPayment(orderId));
    }

    @PostMapping("/payment/failed")
    public ResponseEntity<OrderDto> paymentFailed(@RequestBody UUID orderId) {
        return ResponseEntity.ok(orderService.handlePaymentFailure(orderId));
    }

    @PostMapping("/delivery")
    public ResponseEntity<OrderDto> delivery(@RequestBody UUID orderId) {
        return ResponseEntity.ok(orderService.deliverOrder(orderId));
    }

    @PostMapping("/delivery/failed")
    public ResponseEntity<OrderDto> deliveryFailed(@RequestBody UUID orderId) {
        return ResponseEntity.ok(orderService.handleDeliveryFailure(orderId));
    }

    @PostMapping("/completed")
    public ResponseEntity<OrderDto> complete(@RequestBody UUID orderId) {
        return ResponseEntity.ok(orderService.completeOrder(orderId));
    }

    @PostMapping("/calculate/total")
    public ResponseEntity<OrderDto> calculateTotalCost(@RequestBody UUID orderId) {
        return ResponseEntity.ok(orderService.calculateTotalCost(orderId));
    }

    @PostMapping("/calculate/delivery")
    public ResponseEntity<OrderDto> calculateDeliveryCost(@RequestBody UUID orderId) {
        return ResponseEntity.ok(orderService.calculateDeliveryCost(orderId));
    }

    @PostMapping("/assembly")
    public ResponseEntity<OrderDto> assembly(@RequestBody UUID orderId) {
        return ResponseEntity.ok(orderService.assembleOrder(orderId));
    }

    @PostMapping("/assembly/failed")
    public ResponseEntity<OrderDto> assemblyFailed(@RequestBody UUID orderId) {
        return ResponseEntity.ok(orderService.handleAssemblyFailure(orderId));
    }
}