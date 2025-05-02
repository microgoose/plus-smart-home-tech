package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.exception.NoDeliveryFoundException;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.model.delivery.DeliveryStatus;
import ru.yandex.practicum.repository.DeliveryRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;

    @Override
    @Transactional
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = deliveryMapper.toEntity(deliveryDto);
        delivery.setDeliveryId(UUID.randomUUID());
        delivery.setDeliveryState(DeliveryStatus.CREATED);
        Delivery saved = deliveryRepository.save(delivery);
        return deliveryMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void markDeliverySuccessful(UUID orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException("Delivery not found for order: " + orderId));
        delivery.setDeliveryState(DeliveryStatus.DELIVERED);
        deliveryRepository.save(delivery);
    }

    @Override
    @Transactional
    public void markDeliveryFailed(UUID orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException("Delivery not found for order: " + orderId));
        delivery.setDeliveryState(DeliveryStatus.FAILED);
        deliveryRepository.save(delivery);
    }

    @Override
    @Transactional
    public void markDeliveryPicked(UUID orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException("Delivery not found for order: " + orderId));
        delivery.setDeliveryState(DeliveryStatus.IN_PROGRESS);
        deliveryRepository.save(delivery);
    }

    @Override
    public double calculateDeliveryCost(OrderDto orderDto) {
        final double baseCost = 5.0;
        double result = baseCost;

        // ADDRESS_1 -> *1, ADDRESS_2 -> *2 + baseCost
        if (orderDto.getDeliveryId() != null) {
            Delivery delivery = deliveryRepository.findById(orderDto.getDeliveryId())
                    .orElseThrow(() -> new NoDeliveryFoundException("Delivery not found for id: " + orderDto.getDeliveryId()));
            String warehouseStreet = delivery.getFromAddress().getStreet();
            String destinationStreet = delivery.getToAddress().getStreet();

            if (warehouseStreet.contains("ADDRESS_2")) {
                result *= 2;
                result += baseCost;
            }

            if (orderDto.getFragile() != null && orderDto.getFragile()) {
                result += result * 0.2;
            }

            result += orderDto.getDeliveryWeight() * 0.3;
            result += orderDto.getDeliveryVolume() * 0.2;

            if (!warehouseStreet.equalsIgnoreCase(destinationStreet)) {
                result += result * 0.2;
            }
        }

        return result;
    }
}
