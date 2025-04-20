package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.Pageable;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.model.store.ProductCategory;

import java.util.List;
import java.util.UUID;

//todo
@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public List<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable) {
        return List.of();
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        return null;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        return null;
    }

    @Override
    public boolean removeProduct(UUID productId) {
        return false;
    }

    @Override
    public boolean setProductQuantityState(SetProductQuantityStateRequest request) {
        return false;
    }

    @Override
    public ProductDto getProductById(UUID productId) {
        return null;
    }
}
