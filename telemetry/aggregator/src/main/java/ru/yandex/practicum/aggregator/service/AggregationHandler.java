package ru.yandex.practicum.aggregator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.aggregator.config.KafkaConnector;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationHandler {

    private final AggregationService aggregationService;
    private final KafkaConnector kafkaConnector;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    @Value(value = "${sensorEventTopic}")
    private String sensorTopic;

    @Value(value = "${hubEventTopic}")
    private String hubTopic;

    public void start() {
        try (Consumer<String, SensorEventAvro> consumer = kafkaConnector.getConsumer()) {
            consumer.subscribe(List.of(sensorTopic));

            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = aggregationService.getSensorEvents();
                int count = 0;
                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    handleRecord(record);
                    manageOffsets(record, count, consumer);
                    count++;
                }
                consumer.commitAsync();
            }

        } catch (Exception e) {
            log.error("Ошибка времени обработки событий датчиков", e);
        }

    }

    private void handleRecord(ConsumerRecord<String, SensorEventAvro> consumerRecord) {
        Optional<SensorsSnapshotAvro> snapshotAvro = aggregationService.updateState(consumerRecord.value());
        snapshotAvro.ifPresent(aggregationService::sendSensorSnapshot);
    }

    private void manageOffsets(ConsumerRecord<String, SensorEventAvro> record, int count, Consumer<String, SensorEventAvro> consumer) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );
        if (count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Ошибка времени фиксации оффсета: {}", offsets, exception);
                }
            });
        }
    }

}