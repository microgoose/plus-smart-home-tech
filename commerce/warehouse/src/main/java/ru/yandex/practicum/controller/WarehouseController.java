package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.common.AddressDto;
import ru.yandex.practicum.dto.warehouse.*;
import ru.yandex.practicum.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PutMapping
    public ResponseEntity<Void> registerNewProduct(@RequestBody NewProductInWarehouseRequest request) {
        warehouseService.registerNewProduct(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addProductQuantity(@RequestBody AddProductToWarehouseRequest request) {
        warehouseService.addProductQuantity(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check")
    public ResponseEntity<BookedProductsDto> checkProductAvailability(@RequestBody ShoppingCartDto cart) {
        return ResponseEntity.ok(warehouseService.checkProductAvailability(cart));
    }

    @GetMapping("/address")
    public ResponseEntity<AddressDto> getWarehouseAddress() {
        return ResponseEntity.ok(warehouseService.getWarehouseAddress());
    }

    @PostMapping("/assembly")
    public ResponseEntity<BookedProductsDto> assemblyProductsForOrder(@RequestBody AssemblyProductsForOrderRequest request) {
        return ResponseEntity.ok(warehouseService.assembleProductsForOrder(request));
    }

    @PostMapping("/shipped")
    public ResponseEntity<Void> shippedToDelivery(@RequestBody ShippedToDeliveryRequest request) {
        warehouseService.shipToDelivery(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/return")
    public ResponseEntity<Void> acceptReturn(@RequestBody Map<UUID, Integer> returnedProducts) {
        warehouseService.acceptReturn(returnedProducts);
        return ResponseEntity.ok().build();
    }
}
