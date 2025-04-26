package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.feign.impl.WarehouseClientFallback;

import java.util.UUID;

@FeignClient(name = "warehouse", fallback = WarehouseClientFallback.class)
public interface WarehouseClient extends WarehouseApi {
    @Override
    @PutMapping
    void newProductInWarehouse(@RequestBody AddNewProductInWarehouseRequest request);

    @Override
    @PostMapping("/add")
    void addProductToWarehouse(@RequestBody AddProductToWarehouseRequest request);

    @Override
    @PostMapping("/check")
    BookedProductsDto checkProductQuantityEnoughForShoppingCart(@RequestBody ShoppingCartDto cart);

    @Override
    @GetMapping("/address")
    AddressDto getWarehouseAddress();

    @PostMapping("/assemble")
    void assemblyProductForOrderFromShoppingCart(@RequestBody ShoppingCartDto cart);

    @PostMapping("/return")
    @Override
    void returnProducts(@RequestBody BookedProductsDto bookedProducts);

    @Override
    @PostMapping("/shipped")
    void shippedToDelivery(@RequestParam UUID orderId, @RequestParam UUID deliveryId);


}