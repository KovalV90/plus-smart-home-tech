package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.feign.impl.WarehouseClientFallback;

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
}