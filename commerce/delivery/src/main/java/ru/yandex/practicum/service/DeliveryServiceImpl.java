package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.exception.NoDeliveryFoundException;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.model.DeliveryTariff;
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
        double result = DeliveryTariff.BASE_COST.getValue();

        if (orderDto.getDeliveryId() != null) {
            Delivery delivery = deliveryRepository.findById(orderDto.getDeliveryId())
                    .orElseThrow(() -> new NoDeliveryFoundException("Delivery not found for id: " + orderDto.getDeliveryId()));
            String warehouseStreet = delivery.getFromAddress().getStreet();
            String destinationStreet = delivery.getToAddress().getStreet();

            if (warehouseStreet.contains("ADDRESS_2")) {
                result *= DeliveryTariff.ADDRESS_2_MULTIPLIER.getValue();
                result += DeliveryTariff.BASE_COST.getValue();
            }

            if (Boolean.TRUE.equals(orderDto.getFragile())) {
                result += result * DeliveryTariff.FRAGILE_SURCHARGE_PERCENT.getValue();
            }

            result += orderDto.getDeliveryWeight() * DeliveryTariff.WEIGHT_MULTIPLIER.getValue();
            result += orderDto.getDeliveryVolume() * DeliveryTariff.VOLUME_MULTIPLIER.getValue();

            if (!warehouseStreet.equalsIgnoreCase(destinationStreet)) {
                result += result * DeliveryTariff.DIFFERENT_STREET_SURCHARGE_PERCENT.getValue();
            }
        }

        return result;
    }
}
