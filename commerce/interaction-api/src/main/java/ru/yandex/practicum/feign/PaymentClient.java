package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.feign.impl.PaymentClientFallback;

import java.util.UUID;

@FeignClient(name = "payment", fallback = PaymentClientFallback.class)
public interface PaymentClient extends PaymentApi {

    @Override
    @PutMapping("/api/v1/payment")
    PaymentDto createPayment(@RequestBody PaymentDto dto);

    @Override
    @PostMapping("/api/v1/payment/paid")
    PaymentDto markPaid(@RequestBody UUID id);

    @Override
    @PostMapping("/api/v1/payment/failed")
    PaymentDto markFailed(@RequestBody UUID id);
}
