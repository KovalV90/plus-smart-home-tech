package ru.yandex.practicum.analyzer.config;

import org.apache.kafka.clients.consumer.Consumer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface KafkaConnector {

    Consumer<String, SensorsSnapshotAvro> getSnapshotConsumer();

    Consumer<String, HubEventAvro> getHubEventConsumer();

    void stop();

}
