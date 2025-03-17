package ru.yandex.practicum.aggregator.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.aggregator.consumer.SensorEventConsumer;
import ru.yandex.practicum.aggregator.producer.SnapshotProducer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class AggregationService {
    private final SensorEventConsumer consumer;
    private final SnapshotProducer producer;
    private final Map<String, SensorsSnapshotAvro> snapshots = new ConcurrentHashMap<>();

    public AggregationService(SensorEventConsumer consumer, SnapshotProducer producer) {
        this.consumer = consumer;
        this.producer = producer;
    }

    public void start() {
        try {
            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll();
                records.forEach(record -> processEvent(record.value()));
                consumer.commit();
            }
        } catch (Exception e) {
            log.error("Ошибка при обработке событий: ", e);
        } finally {
            consumer.close();
            producer.close();
        }
    }

    private void processEvent(SensorEventAvro event) {
        if (event == null || event.getHubId() == null || event.getId() == null) {
            log.warn("Пропущено некорректное событие: {}", event);
            return;
        }

        String hubId = event.getHubId();
        String deviceId = event.getId();

        SensorsSnapshotAvro snapshot = snapshots.computeIfAbsent(hubId, id ->
                SensorsSnapshotAvro.newBuilder()
                        .setHubId(hubId)
                        .setTimestamp(event.getTimestamp())
                        .setSensorsState(new HashMap<>())
                        .build()
        );

        SensorStateAvro oldState = snapshot.getSensorsState().get(deviceId);
        SensorStateAvro newState = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();

        if (oldState == null || isNewerAndDifferent(oldState, newState)) {
            snapshot.getSensorsState().put(deviceId, newState);
            snapshot.setTimestamp(event.getTimestamp());

            producer.sendSnapshot(snapshot);
        }
    }

    private boolean isNewerAndDifferent(SensorStateAvro oldState, SensorStateAvro newState) {
        return newState.getTimestamp().isAfter(oldState.getTimestamp()) &&
                !oldState.getData().equals(newState.getData());
    }
}
