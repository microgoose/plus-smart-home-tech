package ru.yandex.practicum.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.client.ShoppingStoreClient;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.model.payment.PaymentStatus;
import ru.yandex.practicum.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ShoppingStoreClient storeClient;
    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;

    @Override
    public double calculateProductCost(OrderDto orderDto) {
        Map<UUID, Long> products = orderDto.getProducts();
        return products.entrySet().stream()
                .map(entry -> {
                    BigDecimal price = storeClient.getProduct(entry.getKey()).getPrice();
                    BigDecimal quantity = BigDecimal.valueOf(entry.getValue());
                    return price.multiply(quantity);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doubleValue();
    }

    @Override
    public double calculateTotalCost(OrderDto orderDto) {
        double productCost = calculateProductCost(orderDto);
        double deliveryCost = orderDto.getDeliveryPrice() != null ? orderDto.getDeliveryPrice() : 0.0;
        double tax = productCost * 0.1;
        return productCost + deliveryCost + tax;
    }

    @Override
    @Transactional
    public PaymentDto createPayment(OrderDto orderDto) {
        double productCost = calculateProductCost(orderDto);
        double deliveryCost = orderDto.getDeliveryPrice() != null ? orderDto.getDeliveryPrice() : 0.0;
        double tax = productCost * 0.1;
        double totalCost = productCost + deliveryCost + tax;

        Payment payment = Payment.builder()
                .paymentId(UUID.randomUUID())
                .orderId(orderDto.getOrderId())
                .productCost(productCost)
                .deliveryCost(deliveryCost)
                .tax(tax)
                .totalCost(totalCost)
                .status(PaymentStatus.PENDING)
                .build();

        paymentRepository.save(payment);

        return PaymentDto.builder()
                .paymentId(payment.getPaymentId())
                .totalPayment(totalCost)
                .deliveryTotal(deliveryCost)
                .feeTotal(tax)
                .build();
    }

    @Override
    @Transactional
    public void markPaymentSuccess(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found: " + paymentId));

        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public void markPaymentFailed(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found: " + paymentId));

        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);

        // Уведомить order-сервис
        orderClient.paymentFailed(paymentId);
    }
}
