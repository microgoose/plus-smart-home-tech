package ru.yandex.practicum.service;

import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.client.ShoppingStoreClient;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.common.AddressDto;
import ru.yandex.practicum.dto.warehouse.*;
import ru.yandex.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.mapper.WarehouseMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.model.ProductReservation;
import ru.yandex.practicum.repository.ProductRepository;
import ru.yandex.practicum.repository.ProductReservationRepository;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final ProductRepository productRepository;
    private final ProductReservationRepository reservationRepository;
    private final WarehouseMapper mapper;
    private final AddressDto currentAddress;
    private final ShoppingStoreClient shoppingStoreClient;

    @Override
    @Transactional
    public void registerNewProduct(NewProductInWarehouseRequest request) {
        if (productRepository.existsById(request.getProductId())) {
            throw new IllegalArgumentException("Продукт уже зарегестрирован на складе");
        }

        // Проверка: товар должен существовать в магазине
        try {
            shoppingStoreClient.getProduct(request.getProductId());
        } catch (FeignException.NotFound e) {
            throw new NoSpecifiedProductInWarehouseException("Товар не найден в магазине: " + request.getProductId());
        }

        Product product = mapper.toEntity(request);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void addProductQuantity(AddProductToWarehouseRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Товар не найден на складе"));
        product.setQuantity(product.getQuantity() + request.getQuantity());
        productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public BookedProductsDto checkProductAvailability(ShoppingCartDto cart) {
        double totalWeight = 0;
        double totalVolume = 0;
        boolean hasFragile = false;

        for (Map.Entry<UUID, Long> entry : cart.getProducts().entrySet()) {
            UUID productId = entry.getKey();
            long requestedQuantity = entry.getValue();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("Товар не найден: " + productId));

            if (product.getQuantity() < requestedQuantity) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Недостаточно товаров: " + productId);
            }

            double volume = product.getWidth() * product.getHeight() * product.getDepth();
            totalVolume += volume * requestedQuantity;
            totalWeight += product.getWeight() * requestedQuantity;

            if (product.isFragile()) {
                hasFragile = true;
            }
        }

        return BookedProductsDto.builder()
                .deliveryVolume(totalVolume)
                .deliveryWeight(totalWeight)
                .fragile(hasFragile)
                .build();
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return currentAddress;
    }

    @Override
    @Transactional
    public BookedProductsDto assembleProductsForOrder(AssemblyProductsForOrderRequest request) {
        UUID orderId = request.getOrderId();
        double totalWeight = 0;
        double totalVolume = 0;
        boolean fragile = false;

        for (Map.Entry<UUID, Long> entry : request.getProducts().entrySet()) {
            UUID productId = entry.getKey();
            Long quantity = entry.getValue();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("Товар не найден: " + productId));

            if (product.getQuantity() < quantity) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Недостаточно товаров: " + productId);
            }

            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);

            ProductReservation reservation = ProductReservation.builder()
                    .orderId(orderId)
                    .product(product)
                    .quantity(quantity)
                    .build();
            reservationRepository.save(reservation);

            totalWeight += product.getWeight() * quantity;
            totalVolume += product.getWidth() * product.getHeight() * product.getDepth() * quantity;
            if (product.isFragile()) {
                fragile = true;
            }
        }

        return BookedProductsDto.builder()
                .deliveryWeight(totalWeight)
                .deliveryVolume(totalVolume)
                .fragile(fragile)
                .build();
    }

    @Override
    @Transactional
    public void shipToDelivery(ShippedToDeliveryRequest request) {
        UUID orderId = request.getOrderId();
        UUID deliveryId = request.getDeliveryId();

        var reservations = reservationRepository.findByOrderId(orderId);
        if (reservations.isEmpty()) {
            throw new EntityNotFoundException("Отсутствуют забронированные товары для заказа: " + orderId);
        }

        for (ProductReservation reservation : reservations) {
            reservation.setDeliveryId(deliveryId);
        }

        reservationRepository.saveAll(reservations);
    }

    @Override
    @Transactional
    public void acceptReturn(Map<UUID, Integer> returnedProducts) {
        for (Map.Entry<UUID, Integer> entry : returnedProducts.entrySet()) {
            UUID productId = entry.getKey();
            int quantity = entry.getValue();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("Товар не найден: " + productId));

            product.setQuantity(product.getQuantity() + quantity);
            productRepository.save(product);
        }
    }
}
