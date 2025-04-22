package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.model.Payment;

@Component
public class PaymentMapper {

    public PaymentDto toDto(Payment payment) {
        return PaymentDto.builder()
                .paymentId(payment.getPaymentId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .username(payment.getUsername())
                .build();
    }
}
