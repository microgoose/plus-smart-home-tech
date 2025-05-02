package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.common.AddressDto;
import ru.yandex.practicum.dto.warehouse.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {

    // Добавление нового товара
    void registerNewProduct(NewProductInWarehouseRequest request);

    // Увеличение количества существующего товара
    void addProductQuantity(AddProductToWarehouseRequest request);

    // Проверка доступности товаров в корзине
    BookedProductsDto checkProductAvailability(ShoppingCartDto cart);

    // Получение текущего адреса склада
    AddressDto getWarehouseAddress();

    // Сборка товаров для заказа
    BookedProductsDto assembleProductsForOrder(AssemblyProductsForOrderRequest request);

    // Передача собранных товаров в доставку
    void shipToDelivery(ShippedToDeliveryRequest request);

    // Возврат товаров на склад
    void acceptReturn(Map<UUID, Integer> returnedProducts);
}
