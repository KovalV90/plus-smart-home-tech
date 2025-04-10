package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.dto.ProductState;
import ru.yandex.practicum.dto.QuantityState;

import java.util.UUID;

@Entity
@Table(name = "products", schema = "public")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    UUID productId;

    @Column(name = "name")
    String productName;

    @Column(name = "description")
    String description;

    @Column(name = "image_src")
    String imageSrc;

    @Column(name = "quantity_state")
    QuantityState quantityState;

    @Column(name = "product_state")
    ProductState productState;

    @Column(name = "product_category")
    ProductCategory productCategory;

    @Column(name = "price")
    Double price;
}
