package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.store.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {

    /**
     * Получить текущую корзину пользователя.
     */
    ShoppingCartDto getCart(String username);

    /**
     * Добавить товары в корзину пользователя.
     */
    ShoppingCartDto addProducts(String username, Map<UUID, Long> productsToAdd);

    /**
     * Удалить товары из корзины.
     */
    ShoppingCartDto removeProducts(String username, List<UUID> productIds);

    /**
     * Изменить количество конкретного товара в корзине.
     */
    ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request);

    /**
     * Деактивировать текущую корзину пользователя.
     */
    void deactivateCart(String username);
}
