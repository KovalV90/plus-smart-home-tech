package ru.yandex.practicum.feign;


import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(name = "shopping-store")
public interface ShoppingStoreClient extends ShoppingStoreApi {
}