package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.feign.impl.DeliveryClientFallback;

import java.util.UUID;

@FeignClient(name = "delivery", fallback = DeliveryClientFallback.class)
public interface DeliveryClient extends DeliveryApi {

    @Override
    @PutMapping("/api/v1/delivery")
    DeliveryDto createDelivery(@RequestBody DeliveryDto dto);

    @Override
    @PostMapping("/api/v1/delivery/delivered")
    DeliveryDto markDelivered(@RequestBody UUID id);

    @Override
    @PostMapping("/api/v1/delivery/failed")
    DeliveryDto markFailed(@RequestBody UUID id);
}
