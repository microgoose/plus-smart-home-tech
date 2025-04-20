package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;

//todo
@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Override
    public AddressDto getWarehouseAddress() {
        return null;
    }

    @Override
    public BookedProductsDto checkProductAvailability(ShoppingCartDto cart) {
        return null;
    }

    @Override
    public void addProductQuantity(AddProductToWarehouseRequest request) {

    }

    @Override
    public void registerNewProduct(NewProductInWarehouseRequest request) {

    }
}


