package ru.yandex.practicum.feign.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.feign.PaymentApi;

import java.util.UUID;

@Slf4j
@Component
public class PaymentClientFallback implements PaymentApi {

    @Override
    public PaymentDto createPayment(PaymentDto dto) {
        log.error("Fallback: createPayment called for {}", dto);
        return null;
    }

    @Override
    public PaymentDto markPaid(UUID id) {
        log.error("Fallback: markPaid called for {}", id);
        return null;
    }

    @Override
    public PaymentDto markFailed(UUID id) {
        log.error("Fallback: markFailed called for {}", id);
        return null;
    }

    @Override
    public Double calculateProductCost(UUID orderId) {
        log.error("Fallback: Ошибка при расчёте стоимости товаров по заказу {}", orderId);
        return 0.0;
    }
}
