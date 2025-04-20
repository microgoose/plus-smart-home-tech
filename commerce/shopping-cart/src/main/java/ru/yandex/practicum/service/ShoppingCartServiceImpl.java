package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.store.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

//todo
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Override
    public void deactivateCart(String username) {
    }

    @Override
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        return null;
    }

    @Override
    public ShoppingCartDto removeProducts(String username, List<UUID> productIds) {
        return null;
    }

    @Override
    public ShoppingCartDto addProducts(String username, Map<UUID, Long> productsToAdd) {
        return null;
    }

    @Override
    public ShoppingCartDto getCart(String username) {
        return null;
    }
}
