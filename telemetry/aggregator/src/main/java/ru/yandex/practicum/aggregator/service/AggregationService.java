package ru.yandex.practicum.aggregator.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.aggregator.config.KafkaConnector;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;


import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AggregationService {

    private final KafkaConnector kafkaConnector;
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();
    private Producer<String, SpecificRecordBase> producer;
    private Consumer<String, SensorEventAvro> consumer;

    @Value(value = "${snapshotTopic}")
    private String snapshotTopic;

    @Value(value = "${pollTimeout}")
    private Integer pollTimeout;

    @PostConstruct
    public void init() {
        this.producer = kafkaConnector.getProducer();
        this.consumer = kafkaConnector.getConsumer();
    }

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        log.info("В Aggregator поступил SensorEventAvro {}", event);

        SensorsSnapshotAvro snapshot;
        if (snapshots.containsKey(event.getHubId())) {
            snapshot = snapshots.get(event.getHubId());
        } else {
            snapshot = SensorsSnapshotAvro.newBuilder()
                    .setHubId(event.getHubId())
                    .setTimestamp(Instant.now())
                    .setSensorsState(new HashMap<>())
                    .build();
        }


        SensorStateAvro oldState = snapshot.getSensorsState().get(event.getId());
        if (oldState != null && (event.getTimestamp().isBefore(oldState.getTimestamp())
                || event.getPayload().equals(oldState.getData()))) {

            log.info("В Aggregator проигнорирован SensorEventAvro {}", event);
            return Optional.empty();
        }


        loadDataToSnapshot(snapshot, event);
        snapshots.put(event.getHubId(), snapshot);

        log.info("В Aggregator сформирован новый Snapshot");
        return Optional.of(snapshot);
    }

    public void sendSensorSnapshot(SensorsSnapshotAvro sensorsSnapshotAvro) {
        log.info("Отправка {} в топик {}", sensorsSnapshotAvro, snapshotTopic);
        producer.send(new ProducerRecord<>(
                snapshotTopic,
                null,
                sensorsSnapshotAvro.getTimestamp().toEpochMilli(),
                sensorsSnapshotAvro.getHubId(),
                sensorsSnapshotAvro)
        );
        log.info("Выполнена отправка {} в топик {}", sensorsSnapshotAvro, snapshotTopic);
    }

    public ConsumerRecords<String, SensorEventAvro> getSensorEvents() {
        ConsumerRecords<String, SensorEventAvro> records = consumer.poll(Duration.ofMillis(pollTimeout));
        return records;
    }

    @PreDestroy
    public void stop() {
        kafkaConnector.stop();
    }

    private SensorsSnapshotAvro loadDataToSnapshot(SensorsSnapshotAvro snapshot, SensorEventAvro event) {
        SensorStateAvro newSensorStateAvro = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();
        snapshot.getSensorsState().put(event.getId(), newSensorStateAvro);
        snapshot.setTimestamp(event.getTimestamp());
        return snapshot;
    }

}

