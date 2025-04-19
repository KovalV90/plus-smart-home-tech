package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.NewProductRequest;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.UpdateProductRequest;
import ru.yandex.practicum.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class ProductMapper {


    public static ProductDto toProductDto(Product product) {
        if (product == null) {
            return null;
        }
        return ProductDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .imageSrc(product.getImageSrc())
                .quantityState(product.getQuantityState())
                .productCategory(product.getProductCategory())
                .productState(product.getProductState())
                .price(product.getPrice())
                .build();
    }


    public static List<ProductDto> toProductDto(Iterable<Product> products) {
        if (products == null) {
            return new ArrayList<>();
        }
        return StreamSupport.stream(products.spliterator(), false)
                .map(ProductMapper::toProductDto)
                .collect(Collectors.toList());
    }


    public static Product toProduct(NewProductRequest newProductRequest) {
        if (newProductRequest == null) {
            return null;
        }
        return Product.builder()
                .productId(newProductRequest.getProductId())
                .productName(newProductRequest.getProductName())
                .description(newProductRequest.getDescription())
                .imageSrc(newProductRequest.getImageSrc())
                .quantityState(newProductRequest.getQuantityState())
                .productCategory(newProductRequest.getProductCategory())
                .productState(newProductRequest.getProductState())
                .price(newProductRequest.getPrice())
                .build();
    }


    public static List<Product> toProductsFromUpdateRequest(Iterable<NewProductRequest> products) {

        if (products == null) {
            return new ArrayList<>();
        }
        return StreamSupport.stream(products.spliterator(), false)
                .map(ProductMapper::toProduct)
                .collect(Collectors.toList());
    }


    public static Product toProduct(UpdateProductRequest updateProductRequest) {
        if (updateProductRequest == null) {
            return null;
        }
        return Product.builder()
                .productId(updateProductRequest.getProductId())
                .productName(updateProductRequest.getProductName())
                .description(updateProductRequest.getDescription())
                .imageSrc(updateProductRequest.getImageSrc())
                .quantityState(updateProductRequest.getQuantityState())
                .productCategory(updateProductRequest.getProductCategory())
                .productState(updateProductRequest.getProductState())
                .price(updateProductRequest.getPrice())
                .build();
    }


    public static List<Product> toProduct(Iterable<UpdateProductRequest> products) {
        if (products == null) {
            return new ArrayList<>();
        }
        return StreamSupport.stream(products.spliterator(), false)
                .map(ProductMapper::toProduct)
                .collect(Collectors.toList());
    }
}
