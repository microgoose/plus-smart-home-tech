package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.service.PaymentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/productCost")
    public ResponseEntity<Double> getProductCost(@RequestBody OrderDto orderDto) {
        double productCost = paymentService.calculateProductCost(orderDto);
        return ResponseEntity.ok(productCost);
    }

    @PostMapping("/totalCost")
    public ResponseEntity<Double> getTotalCost(@RequestBody OrderDto orderDto) {
        double totalCost = paymentService.calculateTotalCost(orderDto);
        return ResponseEntity.ok(totalCost);
    }

    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@RequestBody OrderDto orderDto) {
        PaymentDto payment = paymentService.createPayment(orderDto);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/refund")
    public ResponseEntity<Void> markSuccess(@RequestBody UUID paymentId) {
        paymentService.markPaymentSuccess(paymentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/failed")
    public ResponseEntity<Void> markFailed(@RequestBody UUID paymentId) {
        paymentService.markPaymentFailed(paymentId);
        return ResponseEntity.ok().build();
    }
}