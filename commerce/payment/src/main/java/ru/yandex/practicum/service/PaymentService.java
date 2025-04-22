package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.PaymentDto;

import java.util.UUID;

public interface PaymentService {
    PaymentDto getById(UUID id);
    PaymentDto markAsPaid(UUID id);
    PaymentDto markAsFailed(UUID id);
}
