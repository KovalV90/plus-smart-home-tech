package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.feign.impl.DeliveryClientFallback;

import java.util.UUID;

@FeignClient(name = "delivery", fallback = DeliveryClientFallback.class)
public interface DeliveryClient {


    @PutMapping("/api/v1/delivery")
    DeliveryDto createDelivery(@RequestBody DeliveryDto dto);


    @PostMapping("/api/v1/delivery/successful")
    DeliveryDto markDelivered(@RequestBody UUID orderId);


    @PostMapping("/api/v1/delivery/failed")
    DeliveryDto markFailed(@RequestBody UUID orderId);


    @PostMapping("/api/v1/delivery/cost")
    Double calculateDeliveryCost(@RequestBody OrderDto orderDto);


    @PostMapping("/api/v1/delivery/picked")
    DeliveryDto markPicked(@RequestBody UUID orderId);

}
