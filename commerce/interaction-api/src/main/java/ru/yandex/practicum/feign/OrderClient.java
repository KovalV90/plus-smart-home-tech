package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.OrderDto;

import java.util.UUID;

@FeignClient(name = "order")
public interface OrderClient extends OrderApi {

    @Override
    @PostMapping("/api/v1/order/payment")
    OrderDto markAsPaid(@RequestBody UUID orderId);

    @Override
    @PostMapping("/api/v1/order/payment/failed")
    OrderDto markAsPaymentFailed(@RequestBody UUID orderId);

    @Override
    @PostMapping("/api/v1/order/delivery")
    OrderDto markAsDelivered(@RequestBody UUID orderId);

    @Override
    @PostMapping("/api/v1/order/delivery/failed")
    OrderDto markAsDeliveryFailed(@RequestBody UUID orderId);

    @Override
    @PostMapping("/api/v1/order/completed")
    OrderDto completeOrder(@RequestBody UUID orderId);

    @PostMapping("/api/v1/order/payment/success")
    OrderDto paymentSuccess(@RequestBody UUID orderId);

    @PostMapping("/api/v1/order/payment/failure")
    OrderDto paymentFailed(@RequestBody UUID orderId);

}
