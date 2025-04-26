package ru.yandex.practicum.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryDto {
    private UUID id;
    private AddressDto address;
    private AddressDto fromAddress;
    private String state;
    private UUID orderId;

    private double weight;
    private double volume;
    private boolean fragile;

}
