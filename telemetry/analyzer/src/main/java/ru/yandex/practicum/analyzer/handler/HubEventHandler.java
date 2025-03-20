package ru.yandex.practicum.analyzer.handler;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventHandler {

    String getEventType();

    void handle(HubEventAvro event);

}
