package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {
    @Id
    @GeneratedValue
    private UUID id;

    private String address;
    @Enumerated(EnumType.STRING)
    private DeliveryState state;
    private UUID orderId;
}
