package ru.yandex.practicum.feign;

import ru.yandex.practicum.dto.PaymentDto;

import java.util.UUID;

public interface PaymentApi {

    PaymentDto createPayment(PaymentDto dto);

    PaymentDto markPaid(UUID id);

    PaymentDto markFailed(UUID id);
    Double calculateProductCost(UUID orderId);
}
