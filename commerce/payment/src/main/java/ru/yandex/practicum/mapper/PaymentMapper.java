package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.model.PaymentStatus;

@Component
public class PaymentMapper {

    public PaymentDto toDto(Payment payment) {
        return PaymentDto.builder()
                .paymentId(payment.getPaymentId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .deliveryPrice(payment.getDeliveryPrice())
                .totalPrice(payment.getTotalPrice())
                .status(String.valueOf(payment.getStatus()))
                .build();
    }
    public Payment toEntity(PaymentDto dto) {
        return Payment.builder()
                .paymentId(dto.getPaymentId())
                .orderId(dto.getOrderId())
                .amount(dto.getAmount())
                .deliveryPrice(dto.getDeliveryPrice())
                .totalPrice(dto.getTotalPrice())
                .status(PaymentStatus.valueOf(dto.getStatus()))
                .build();
    }
}
