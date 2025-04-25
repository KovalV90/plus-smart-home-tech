package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.feign.impl.PaymentClientFallback;

import java.util.UUID;

@FeignClient(name = "payment", fallback = PaymentClientFallback.class)
public interface PaymentClient {


    @PutMapping("/api/v1/payment")
    PaymentDto createPayment(@RequestBody PaymentDto dto);


    @PostMapping("/api/v1/payment/refund")
    PaymentDto paymentSuccess(@RequestBody UUID paymentId);

    @PostMapping("/api/v1/payment/failed")
    PaymentDto paymentFailed(@RequestBody UUID paymentId);


    @PostMapping("/api/v1/payment/productCost")
    Double calculateProductCost(@RequestBody UUID orderId);

    @PostMapping("/api/v1/payment/totalCost")
    Double calculateTotalCost(@RequestBody UUID orderId);

}
