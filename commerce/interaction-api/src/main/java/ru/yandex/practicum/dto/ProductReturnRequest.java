package ru.yandex.practicum.dto;


import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReturnRequest {
    private UUID orderId;
    private Map<UUID, Long> products;
}
