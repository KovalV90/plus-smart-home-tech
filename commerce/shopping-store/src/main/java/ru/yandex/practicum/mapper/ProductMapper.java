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
        ProductDto productDto = new ProductDto();
        productDto.setProductId(product.getProductId());
        productDto.setProductName(product.getProductName());
        productDto.setDescription(product.getDescription());
        productDto.setImageSrc(product.getImageSrc());
        productDto.setQuantityState(product.getQuantityState());
        productDto.setProductCategory(product.getProductCategory());
        productDto.setProductState(product.getProductState());
        productDto.setPrice(product.getPrice());
        return productDto;
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
        Product product = new Product();
        product.setProductId(newProductRequest.getProductId());
        product.setProductName(newProductRequest.getProductName());
        product.setDescription(newProductRequest.getDescription());
        product.setImageSrc(newProductRequest.getImageSrc());
        product.setQuantityState(newProductRequest.getQuantityState());
        product.setProductCategory(newProductRequest.getProductCategory());
        product.setProductState(newProductRequest.getProductState());
        product.setPrice(newProductRequest.getPrice());
        return product;
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
        Product product = new Product();
        product.setProductId(updateProductRequest.getProductId());
        product.setProductName(updateProductRequest.getProductName());
        product.setDescription(updateProductRequest.getDescription());
        product.setImageSrc(updateProductRequest.getImageSrc());
        product.setQuantityState(updateProductRequest.getQuantityState());
        product.setProductCategory(updateProductRequest.getProductCategory());
        product.setProductState(updateProductRequest.getProductState());
        product.setPrice(updateProductRequest.getPrice());
        return product;
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
