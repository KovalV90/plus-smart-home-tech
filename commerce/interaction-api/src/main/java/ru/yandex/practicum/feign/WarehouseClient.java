package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;

import ru.yandex.practicum.feign.impl.WarehouseClientFallback;

@FeignClient(name = "warehouse", fallback = WarehouseClientFallback.class)
public interface WarehouseClient extends WarehouseApi {
}