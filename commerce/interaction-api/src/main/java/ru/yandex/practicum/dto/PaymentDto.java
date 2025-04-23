package ru.yandex.practicum.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {
    private UUID paymentId;
    private UUID orderId;
    private Double amount;
    private String status;
    private String username;
    private Double deliveryPrice;
    private Double totalPrice;
}
