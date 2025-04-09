package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.feign.StoreClient;
import ru.yandex.practicum.service.ProductService;

import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${ShoppingStore.api.prefix}")
public class ShoppingStoreController implements StoreClient {

    private final ProductService productService;

    @Value("${ShoppingStore.api.prefix}")
    private String prefix;


    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createNewProduct(@Valid @RequestBody NewProductRequest newProductRequest) {
        log.info(">>> [PUT {}] Вход в createNewProduct: {}", prefix, newProductRequest);
        ProductDto response = productService.createNewProduct(newProductRequest);
        log.info("<<< [PUT {}] Товар создан: {}", prefix, response);
        return response;
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getProductsByCategory(@RequestParam ProductCategory category,
                                                  @RequestParam Pageable pageable) {
        log.info(">>> [GET {}] Вход в getProductsByCategory: category={}, pageable={}", prefix, category, pageable);
        List<ProductDto> response = productService.getProductsByParams(category, pageable);
        log.info("<<< [GET {}] Товары получены: {}", prefix, response);
        return response;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto updateProduct(@Valid @RequestBody UpdateProductRequest updateProductRequest) {
        log.info(">>> [POST {}] Вход в updateProduct: {}", prefix, updateProductRequest);
        ProductDto response = productService.updateProduct(updateProductRequest);
        log.info("<<< [POST {}] Товар обновлён: {}", prefix, response);
        return response;
    }


    @PostMapping("/removeProductFromStore")
    @ResponseStatus(HttpStatus.OK)
    public void removeProductFromStore(@RequestBody UUID id) {
        log.info(">>> [POST {}/removeProductFromStore] Вход: id={}", prefix, id);
        productService.removeProductFromStore(id);
        log.info("<<< [POST {}/removeProductFromStore] Товар деактивирован: id={}", prefix, id);
    }


    @Override
    @PostMapping("/quantityState")
    @ResponseStatus(HttpStatus.OK)
    public void setProductQuantityState(@Valid @RequestBody SetProductQuantityStateRequest request) {
        log.info(">>> [POST {}/quantityState] Вход: {}", prefix, request);
        productService.setProductQuantityState(request);
        log.info("<<< [POST {}/quantityState] Статус остатка изменён: {}", prefix, request);
    }


    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getProductById(@PathVariable UUID productId) {
        log.info(">>> [GET {}/{}] Вход в getProductById: productId={}", prefix, productId, productId);
        ProductDto response = productService.getProduct(productId);
        log.info("<<< [GET {}/{}] Товар получен: {}", prefix, productId, response);
        return response;
    }
}
