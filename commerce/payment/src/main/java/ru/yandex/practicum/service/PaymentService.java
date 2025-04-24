package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.PaymentDto;

import java.util.UUID;

public interface PaymentService {
    PaymentDto createPayment(PaymentDto dto);

    Double calculateProductCost(UUID orderId);

    Double calculateTotalCost(UUID orderId);

    PaymentDto paymentSuccess(UUID paymentId);

    PaymentDto paymentFailed(UUID paymentId);
}
