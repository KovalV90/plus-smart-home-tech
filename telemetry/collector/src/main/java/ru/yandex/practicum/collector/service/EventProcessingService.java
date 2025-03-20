package ru.yandex.practicum.collector.service;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.collector.kafka.KafkaProducerManager;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventProcessingService {
    private final KafkaProducerManager kafkaClient;

    @Value(value = "${sensorEventTopic}")
    private String sensorsTopic;

    @Value(value = "${hubEventTopic}")
    private String hubTopic;

    public void sendSensorEvent(SensorEventAvro event) {
        log.info("Отправка {} в топик {}", event, sensorsTopic);
        kafkaClient.getProducer().send(new ProducerRecord<>(
                sensorsTopic,
                null,
                event.getTimestamp().toEpochMilli(),
                event.getHubId(),
                event)
        );
        log.info("Выполнена отправка {} в топик {}", event, sensorsTopic);
    }

    public void sendHubEvent(HubEventAvro event) {
        log.info("Отправка {} в топик {}", event, sensorsTopic);
        kafkaClient.getProducer().send(new ProducerRecord<>(
                hubTopic,
                null,
                event.getTimestamp().toEpochMilli(),
                event.getHubId(),
                event)
        );
        log.info("Выполнена отправка {} в топик {}", event, sensorsTopic);
    }

    @PreDestroy
    public void stop() {
        kafkaClient.stop();
    }

}
