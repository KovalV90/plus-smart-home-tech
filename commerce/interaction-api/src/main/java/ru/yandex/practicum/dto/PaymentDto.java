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
    private Double amount;
    private String status;
    private String username;
}
