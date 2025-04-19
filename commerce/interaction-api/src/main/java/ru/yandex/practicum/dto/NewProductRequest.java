package ru.yandex.practicum.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class NewProductRequest {
    UUID productId;

    @NotBlank(message = "Название товара не может быть пустым")
    String productName;

    @NotBlank(message = "Описание товара не может быть пустым")
    String description;

    String imageSrc;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Состояние остатка товара обязательно")
    QuantityState quantityState;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Статус товара обязателен")
    ProductState productState;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Категория товара обязательна")
    ProductCategory productCategory;

    @Min(value = 1, message = "Цена не может быть меньше 1")
    Double price;
}