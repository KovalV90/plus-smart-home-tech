package ru.yandex.practicum.feign;

import ru.yandex.practicum.dto.PaymentDto;

import java.util.UUID;

public interface PaymentApi {

    PaymentDto createPayment(PaymentDto dto);

    PaymentDto paymentSuccess(UUID paymentId);
    PaymentDto paymentFailed(UUID paymentId);
    Double calculateProductCost(UUID orderId);

    Double calculateTotalCost(UUID orderId);
}
