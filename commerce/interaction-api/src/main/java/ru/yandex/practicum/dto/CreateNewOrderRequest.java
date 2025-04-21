package ru.yandex.practicum.dto;

import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNewOrderRequest {
    private ShoppingCartDto shoppingCart;
    private AddressDto deliveryAddress;
    String username;
}
