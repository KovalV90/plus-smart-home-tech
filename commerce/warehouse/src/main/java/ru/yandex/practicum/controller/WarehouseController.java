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

import java.util.UUID;

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

    @Override
    @PostMapping("/assembly")
    @ResponseStatus(HttpStatus.OK)
    public void assemblyProductForOrderFromShoppingCart(@RequestBody ShoppingCartDto cart) {
        log.info(">>> [POST {}/assembly] Начало сборки товаров по корзине: {}", prefix, cart);
        warehouseService.assemblyProductForOrderFromShoppingCart(cart);
        log.info("<<< [POST {}/assembly] Завершение сборки товаров");
    }

    @Override
    @PostMapping("/return")
    @ResponseStatus(HttpStatus.OK)
    public void returnProducts(@RequestBody BookedProductsDto bookedProducts) {
        log.info(">>> [POST {}/return] Начало возврата товаров на склад: {}", prefix, bookedProducts);
        warehouseService.returnProducts(bookedProducts);
        log.info("<<< [POST {}/return] Завершение возврата товаров на склад");

    }

    @PostMapping("/shipped/{orderId}/{deliveryId}")
    @ResponseStatus(HttpStatus.OK)
    public void shippedToDelivery(@PathVariable UUID orderId, @PathVariable UUID deliveryId) {
        log.info(">>> [POST {}/shipped] Передача в доставку: orderId = {}, deliveryId = {}", prefix, orderId, deliveryId);
        warehouseService.shippedToDelivery(orderId, deliveryId);
        log.info("<<< [POST {}/shipped] Передача завершена", prefix);
    }


}
