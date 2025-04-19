package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ProductRepository;
import ru.yandex.practicum.service.ProductService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    @Transactional
    @Override
    public ProductDto createNewProduct(NewProductRequest newProductRequest) {
        log.info("Начало создания нового продукта. Request: {}", newProductRequest);
        Product product = ProductMapper.toProduct(newProductRequest);
        Product savedProduct = productRepository.save(product);
        ProductDto productDto = ProductMapper.toProductDto(savedProduct);
        log.info("Продукт успешно создан: {}", productDto);
        return productDto;
    }


    @Override
    public List<ProductDto> getProductsByParams(ProductCategory category, Pageable pageable) {
        log.info("Запрос списка продуктов. Категория: {}, Pageable: {}", category, pageable);
        List<Product> products = productRepository.findAllByProductCategory(category, pageable);
        List<ProductDto> productDtos = ProductMapper.toProductDto(products);
        log.info("Получено {} продуктов по категории {}", productDtos.size(), category);
        return productDtos;
    }


    @Override
    public ProductDto getProduct(UUID id) {
        log.info("Получение продукта по id: {}", id);
        Product product = findOrThrowNotFound(id);
        ProductDto productDto = ProductMapper.toProductDto(product);
        log.info("Продукт найден: {}", productDto);
        return productDto;
    }


    @Transactional
    @Override
    public ProductDto updateProduct(UpdateProductRequest updateProductRequest) {
        UUID id = updateProductRequest.getProductId();
        log.info("Начало обновления продукта с id: {}. Request: {}", id, updateProductRequest);
        Product existProduct = findOrThrowNotFound(id);


        if (updateProductRequest.getImageSrc() == null || updateProductRequest.getImageSrc().isBlank()) {
            updateProductRequest.setImageSrc(existProduct.getImageSrc());
            log.info("Поле imageSrc отсутствует в запросе, использовано значение из БД: {}", existProduct.getImageSrc());

        }
        if (updateProductRequest.getProductCategory() == null) {
            updateProductRequest.setProductCategory(existProduct.getProductCategory());
            log.info("Поле productCategory отсутствует в запросе, использовано значение из БД: {}", existProduct.getProductCategory());
        }

        Product updatedProduct = ProductMapper.toProduct(updateProductRequest);
        Product savedProduct = productRepository.save(updatedProduct);
        ProductDto productDto = ProductMapper.toProductDto(savedProduct);
        log.info("Продукт успешно обновлен: {}", productDto);
        return productDto;
    }


    @Transactional
    @Override
    public void setProductQuantityState(SetProductQuantityStateRequest request) {
        log.info("Запрос на изменение состояния количества для продукта с id: {}. Новый статус: {}",
                request.getProductId(), request.getQuantityState());
        Product existProduct = findOrThrowNotFound(request.getProductId());
        existProduct.setQuantityState(request.getQuantityState());
        productRepository.save(existProduct);
        log.info("Состояние количества продукта обновлено для id: {}", request.getProductId());
    }


    @Transactional
    @Override
    public Boolean removeProductFromStore(UUID id) {
        log.info("Запрос на деактивацию продукта с id: {}", id);
        Product existProduct = findOrThrowNotFound(id);
        existProduct.setProductState(ProductState.DEACTIVATE);
        productRepository.save(existProduct);
        log.info("Продукт с id: {} успешно деактивирован", id);
        return true;
    }


    private Product findOrThrowNotFound(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    String errMsg = "Продукт с id=" + id + " не найден";
                    log.error(errMsg);
                    return new NotFoundException(errMsg);
                });
    }
}
