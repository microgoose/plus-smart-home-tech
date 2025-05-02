package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.service.DeliveryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PutMapping
    public ResponseEntity<DeliveryDto> planDelivery(@RequestBody DeliveryDto deliveryDto) {
        DeliveryDto result = deliveryService.createDelivery(deliveryDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/successful")
    public ResponseEntity<Void> markSuccessful(@RequestBody UUID orderId) {
        deliveryService.markDeliverySuccessful(orderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/failed")
    public ResponseEntity<Void> markFailed(@RequestBody UUID orderId) {
        deliveryService.markDeliveryFailed(orderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/picked")
    public ResponseEntity<Void> markPicked(@RequestBody UUID orderId) {
        deliveryService.markDeliveryPicked(orderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cost")
    public ResponseEntity<Double> calculateCost(@RequestBody OrderDto orderDto) {
        double cost = deliveryService.calculateDeliveryCost(orderDto);
        return ResponseEntity.ok(cost);
    }

}