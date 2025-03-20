package ru.yandex.practicum.analyzer.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Slf4j
@Component
public class SnapshotProducer {
    private final KafkaProducer<String, SensorsSnapshotAvro> producer;

    public SnapshotProducer(Properties producerProps) {
        this.producer = new KafkaProducer<>(producerProps);
    }

    public void sendSnapshot(SensorsSnapshotAvro snapshot) {
        try {
            ProducerRecord<String, SensorsSnapshotAvro> record =
                    new ProducerRecord<>("telemetry.snapshots.v1", snapshot.getHubId(), snapshot);
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    log.error("Ошибка отправки снапшота в Kafka", exception);
                } else {
                    log.info("Снапшот отправлен в partition={}, offset={}", metadata.partition(), metadata.offset());
                }
            });
        } catch (Exception e) {
            log.error("Ошибка при отправке снапшота в Kafka", e);
        }
    }

    public void close() {
        producer.close();
    }
}
