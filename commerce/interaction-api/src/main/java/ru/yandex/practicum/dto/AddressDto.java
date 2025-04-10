package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    @NotBlank(message = "Страна не может быть пустой")
    String country;
    @NotBlank(message = "Город не может быть пустым")
    String city;
    @NotBlank(message = "Улица не может быть пустой")
    String street;
    @NotBlank(message = "Дом не может быть пустым")
    String house;

    String flat;
}