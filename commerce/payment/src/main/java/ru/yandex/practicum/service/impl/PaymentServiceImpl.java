package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.exception.NoPaymentFoundException;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.model.PaymentStatus;
import ru.yandex.practicum.repository.PaymentRepository;
import ru.yandex.practicum.service.PaymentService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentDto getById(UUID id) {
        return paymentMapper.toDto(findById(id));
    }

    @Override
    public PaymentDto markAsPaid(UUID id) {
        Payment payment = findById(id);
        payment.setStatus(PaymentStatus.PAID.name());
        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    @Override
    public PaymentDto markAsFailed(UUID id) {
        Payment payment = findById(id);
        payment.setStatus(PaymentStatus.FAILED.name());
        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    private Payment findById(UUID id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new NoPaymentFoundException("Платеж не найден(id): " + id));
    }
}
