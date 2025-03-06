package ru.yandex.practicum.collector.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.collector.kafka.KafkaCollectorProducer;
import ru.yandex.practicum.collector.model.HubEvent;
import ru.yandex.practicum.collector.model.SensorEvent;

@Service
public class CollectorService {
    private final KafkaCollectorProducer kafkaProducer;

    public CollectorService(KafkaCollectorProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public void processSensorEvent(SensorEvent event) {
        kafkaProducer.sendSensorEvent("telemetry.sensors.v1", event);
    }

    public void processHubEvent(HubEvent event) {
        kafkaProducer.sendHubEvent("telemetry.hubs.v1", event);
    }
}
