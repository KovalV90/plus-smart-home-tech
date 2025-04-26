package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.feign.PaymentClient;
import ru.yandex.practicum.service.PaymentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController implements PaymentClient {

    private final PaymentService paymentService;

    @Override
    @PutMapping
    public PaymentDto createPayment(@RequestBody PaymentDto dto) {
        return paymentService.createPayment(dto);
    }

    @Override
    @PostMapping("/productCost")
    public Double calculateProductCost(@RequestBody UUID orderId) {
        return paymentService.calculateProductCost(orderId);
    }

    @Override
    @PostMapping("/totalCost")
    public Double calculateTotalCost(@RequestBody UUID orderId) {
        return paymentService.calculateTotalCost(orderId);
    }

    @Override
    @PostMapping("/refund")
    public PaymentDto paymentSuccess(@RequestBody UUID paymentId) {
        return paymentService.paymentSuccess(paymentId);
    }

    @Override
    @PostMapping("/failed")
    public PaymentDto paymentFailed(@RequestBody UUID paymentId) {
        return paymentService.paymentFailed(paymentId);
    }
}
