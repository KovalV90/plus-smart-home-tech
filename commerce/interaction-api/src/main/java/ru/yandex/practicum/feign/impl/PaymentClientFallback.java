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
    public PaymentDto paymentSuccess(UUID paymentId) {
        log.error("Fallback: вызов paymentSuccess для платежа {}. Сервис оплаты недоступен.", paymentId);
        return null;
    }

    @Override
    public PaymentDto paymentFailed(UUID paymentId) {
        log.error("Fallback: вызов paymentFailed для платежа {}. Сервис оплаты недоступен.", paymentId);
        return null;
    }


    @Override
    public Double calculateProductCost(UUID orderId) {
        log.error("Fallback: Ошибка при расчёте стоимости товаров по заказу {}", orderId);
        return 0.0;
    }

    @Override
    public Double calculateTotalCost(UUID orderId) {
        log.error("Fallback: ошибка при расчёте общей стоимости заказа {}. Сервис оплаты недоступен.", orderId);
        return 0.0;
    }
}
