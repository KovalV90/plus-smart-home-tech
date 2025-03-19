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
        if (event == null || event.getHubId() == null || event.getId() == null || event.getPayload() == null) {
            log.warn("Пропущено некорректное событие: {}", event);
            return;
        }

        String hubId = event.getHubId();
        String deviceId = event.getId();
        String payload = event.getPayload().toString();

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
                .setData(payload)
                .build();

        if (oldState == null || isNewerAndDifferent(oldState, newState)) {
            snapshot.getSensorsState().put(deviceId, newState);
            snapshot.setTimestamp(event.getTimestamp());
        }

        sendSnapshot(snapshot);
    }


    private boolean isNewerAndDifferent(SensorStateAvro oldState, SensorStateAvro newState) {
        return newState.getTimestamp().isAfter(oldState.getTimestamp()) &&
                !oldState.getData().equals(newState.getData());
    }

    private void sendSnapshot(SensorsSnapshotAvro snapshot) {
        int maxRetries = 3;
        int attempt = 0;
        boolean success = false;

        while (attempt < maxRetries && !success) {
            try {
                producer.sendSnapshot(snapshot);
                log.info("Снапшот успешно отправлен в Kafka для хаба {}", snapshot.getHubId());
                success = true;
            } catch (Exception e) {
                attempt++;
                log.error("Ошибка при отправке снапшота в Kafka (попытка {}/{}): {}", attempt, maxRetries, e.getMessage());
            }
        }

        if (!success) {
            log.error("Не удалось отправить снапшот после {} попыток: {}", maxRetries, snapshot);
        }
    }

    private String normalizeSensorData(String payload) {
        if (payload == null || payload.isEmpty()) {
            return "{}"; // Возвращаем пустой JSON
        }

        // Дополнительные проверки значений
        if (payload.contains("\"humidity\":101") || payload.contains("\"co2Level\":0")) {
            log.warn("Обнаружены некорректные данные в payload: {}", payload);
        }

        return payload; // Гарантируем, что вернётся строка
    }
}
