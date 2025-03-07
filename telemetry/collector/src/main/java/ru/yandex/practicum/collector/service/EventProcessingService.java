package ru.yandex.practicum.collector.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.collector.converter.HubEventAvroConverter;
import ru.yandex.practicum.collector.converter.SensorEventAvroConverter;
import ru.yandex.practicum.collector.kafka.KafkaCollectorProducer;
import ru.yandex.practicum.collector.kafka.TopicType;
import ru.yandex.practicum.collector.model.HubEvent;
import ru.yandex.practicum.collector.model.SensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Service
public class EventProcessingService {
    private final KafkaCollectorProducer kafkaProducer;

    public EventProcessingService(KafkaCollectorProducer kafkaProducer) {

        this.kafkaProducer = kafkaProducer;
    }

    public void processSensorEvent(SensorEvent event) {

        SensorEventAvro sensorEventAvro = SensorEventAvroConverter.convert(event);
        kafkaProducer.send(
                sensorEventAvro,
                event.getHubId(),
                event.getTimestamp(),
                TopicType.SENSORS
        );
    }

    public void processHubEvent(HubEvent event) {
        HubEventAvro hubEventAvro = HubEventAvroConverter.convert(event);
        kafkaProducer.send(
                hubEventAvro,
                event.getHubId(),
                event.getTimestamp(),
                TopicType.HUBS
        );
    }
}
