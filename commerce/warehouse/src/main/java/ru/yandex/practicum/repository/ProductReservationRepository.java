package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.ProductReservation;

import java.util.List;
import java.util.UUID;

public interface ProductReservationRepository extends JpaRepository<ProductReservation, UUID> {

    List<ProductReservation> findByOrderId(UUID orderId);
}
