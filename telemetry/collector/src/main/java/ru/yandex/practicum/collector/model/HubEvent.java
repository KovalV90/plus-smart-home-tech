package ru.yandex.practicum.collector.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class HubEvent {
    @NotBlank
    private String hubId;

    @NotNull
    private Instant timestamp = Instant.now();

    @NotNull
    private HubEventType type;
}
