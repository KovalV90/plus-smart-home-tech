package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.feign.WarehouseApi;
import ru.yandex.practicum.service.WarehouseService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${Warehouse.api.prefix}")
public class WarehouseController implements WarehouseApi {
    private final WarehouseService warehouseService;

    @Value("${Warehouse.api.prefix}")
    private String prefix;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void newProductInWarehouse(@RequestBody @Valid AddNewProductInWarehouseRequest request) {
        log.info(">>> [PUT {}] Начало операции добавления нового товара на склад. Request = {}", prefix, request);
        warehouseService.newProductInWarehouse(request);
        log.info("<<< [PUT {}] Завершение операции добавления нового товара. Request = {}", prefix, request);
    }

    @GetMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    public AddressDto getWarehouseAddress() {
        log.info(">>> [GET {}/address] Начало операции получения адреса склада", prefix);
        AddressDto response = warehouseService.getWarehouseAddress();
        log.info("<<< [GET {}/address] Завершение операции получения адреса склада. Response = {}", prefix, response);
        return response;
    }


    @Override
    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(@RequestBody ShoppingCartDto cart) {
        log.info(">>> [POST {}/check] Начало проверки остатков для корзины: {}", prefix, cart);
        BookedProductsDto response = warehouseService.checkProductQuantityEnoughForShoppingCart(cart);
        log.info("<<< [POST {}/check] Завершение проверки остатков. Response = {}", prefix, response);
        return response;
    }


    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void addProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest request) {
        log.info(">>> [POST {}/add] Начало пополнения остатков товара. Request = {}", prefix, request);
        warehouseService.addProductToWarehouse(request);
        log.info("<<< [POST {}/add] Завершение пополнения остатков товара. Request = {}", prefix, request);
    }
}
