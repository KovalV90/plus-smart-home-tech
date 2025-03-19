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

                if (records.isEmpty()) {
                    log.warn("Нет новых событий в telemetry.sensors.v1");
                    continue;
                }

                log.info("Получено {} событий от сенсоров", records.count());

                records.forEach(record -> processEvent(record.value()));
                consumer.commit();
            }
        } catch (Exception e) {
            log.error("Критическая ошибка в AggregationService!", e);
        } finally {
            log.info("Закрываем Kafka consumer и producer...");
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

        // Если данные изменились или устройство новое — обновляем
        if (oldState == null || isNewerAndDifferent(oldState, newState)) {
            snapshot.getSensorsState().put(deviceId, newState);
            snapshot.setTimestamp(event.getTimestamp());

            sendSnapshot(snapshot);
        } else {
            // Теперь снапшот отправляется всегда, даже если данные не изменились (по заданию)
            log.info("Данные сенсора {} не изменились, но отправляем снапшот", deviceId);
            sendSnapshot(snapshot);
        }
    }

    private boolean isNewerAndDifferent(SensorStateAvro oldState, SensorStateAvro newState) {
        return newState.getTimestamp().isAfter(oldState.getTimestamp()) &&
                !oldState.getData().equals(newState.getData());
    }

    private void sendSnapshot(SensorsSnapshotAvro snapshot) {
        try {
            producer.sendSnapshot(snapshot);
            log.info("Снапшот успешно отправлен в Kafka для хаба {}", snapshot.getHubId());
        } catch (Exception e) {
            log.error("Ошибка при отправке снапшота в Kafka для хаба {}", snapshot.getHubId(), e);
        }
    }
}
