package ru.yandex.practicum.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.store.ChangeProductQuantityRequest;
import ru.yandex.practicum.mapper.ShoppingCartMapper;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.model.ShoppingCartItem;
import ru.yandex.practicum.repository.ShoppingCartItemRepository;
import ru.yandex.practicum.repository.ShoppingCartRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository cartRepository;
    private final ShoppingCartItemRepository itemRepository;
    private final ShoppingCartMapper cartMapper;

    @Override
    @Transactional
    public ShoppingCartDto getCart(String username) {
        ShoppingCart cart = cartRepository.findByUsernameAndActiveTrue(username)
                .orElseGet(() -> {
                    ShoppingCart newCart = ShoppingCart.builder()
                            .username(username)
                            .active(true)
                            .build();
                    return cartRepository.save(newCart);
                });

        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public ShoppingCartDto addProducts(String username, Map<UUID, Long> productsToAdd) {
        ShoppingCart cart = cartRepository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new IllegalStateException("No active cart found"));

        if (!cart.isActive()) {
            throw new IllegalStateException("Cart is deactivated");
        }

        for (Map.Entry<UUID, Long> entry : productsToAdd.entrySet()) {
            UUID productId = entry.getKey();
            long quantity = entry.getValue();

            ShoppingCartItem item = itemRepository.findByCartIdAndProductId(cart.getId(), productId)
                    .orElse(ShoppingCartItem.builder()
                            .cart(cart)
                            .productId(productId)
                            .quantity(0)
                            .build());

            item.setQuantity(item.getQuantity() + quantity);
            itemRepository.save(item);
        }

        return cartMapper.toDto(cartRepository.findById(cart.getId()).orElseThrow());
    }

    @Override
    @Transactional
    public ShoppingCartDto removeProducts(String username, List<UUID> productIds) {
        ShoppingCart cart = cartRepository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new IllegalStateException("No active cart found"));

        for (UUID productId : productIds) {
            itemRepository.findByCartIdAndProductId(cart.getId(), productId)
                    .ifPresent(itemRepository::delete);
        }

        return cartMapper.toDto(cartRepository.findById(cart.getId()).orElseThrow());
    }

    @Override
    @Transactional
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        ShoppingCart cart = cartRepository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new IllegalStateException("No active cart found"));

        ShoppingCartItem item = itemRepository.findByCartIdAndProductId(cart.getId(), request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not in cart"));

        item.setQuantity(request.getNewQuantity());
        itemRepository.save(item);

        return cartMapper.toDto(cartRepository.findById(cart.getId()).orElseThrow());
    }

    @Override
    @Transactional
    public void deactivateCart(String username) {
        ShoppingCart cart = cartRepository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new IllegalStateException("No active cart to deactivate"));

        cart.setActive(false);
        cartRepository.save(cart);
    }
}
