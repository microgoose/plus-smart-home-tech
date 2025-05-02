package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.model.payment.PaymentStatus;

import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "product_cost", nullable = false)
    private double productCost;

    @Column(name = "delivery_cost", nullable = false)
    private double deliveryCost;

    @Column(name = "tax", nullable = false)
    private double tax;

    @Column(name = "total_cost", nullable = false)
    private double totalCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;
}