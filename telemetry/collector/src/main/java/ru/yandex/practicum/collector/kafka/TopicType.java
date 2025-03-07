package ru.yandex.practicum.collector.kafka;

import lombok.Getter;

@Getter
public enum TopicType {
    SENSORS("telemetry.sensors.v1"),
    HUBS("telemetry.hubs.v1");

    private final String topicName;

    TopicType(String topicName) {
        this.topicName = topicName;
    }

}
