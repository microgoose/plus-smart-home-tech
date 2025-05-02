package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.common.AddressDto;
import ru.yandex.practicum.dto.warehouse.*;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseClient {

    /**
     * Получить текущий адрес склада.
     */
    @GetMapping("/address")
    AddressDto getWarehouseAddress();

    /**
     * Проверить наличие товаров на складе по корзине.
     */
    @PostMapping("/check")
    BookedProductsDto checkProductAvailability(@RequestBody ShoppingCartDto cart);

    /**
     * Добавить существующий товар на склад.
     */
    @PostMapping("/add")
    void addProductQuantity(@RequestBody AddProductToWarehouseRequest request);

    /**
     * Зарегистрировать новый товар на складе.
     */
    @PutMapping
    void registerNewProduct(@RequestBody NewProductInWarehouseRequest request);

    /**
     * Отправить товары в доставку (привязать deliveryId к заказу).
     */
    @PostMapping("/shipped")
    void shippedToDelivery(@RequestBody ShippedToDeliveryRequest request);

    /**
     * Вернуть товары на склад (увеличить остатки).
     */
    @PostMapping("/return")
    void acceptReturn(@RequestBody Map<UUID, Integer> returnedProducts);

    /**
     * Собрать заказ — забронировать товары под заказ и уменьшить остатки.
     */
    @PostMapping("/assembly")
    BookedProductsDto assemblyProductsForOrder(@RequestBody AssemblyProductsForOrderRequest request);

}
