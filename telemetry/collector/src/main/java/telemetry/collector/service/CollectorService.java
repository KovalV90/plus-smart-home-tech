package telemetry.collector.service;

import telemetry.collector.kafka.KafkaCollectorProducer;
import telemetry.collector.model.SensorEvent;
import telemetry.collector.model.HubEvent;
import org.springframework.stereotype.Service;

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
