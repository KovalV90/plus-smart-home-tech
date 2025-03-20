package ru.yandex.practicum.analyzer.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class KafkaClient {

    private final KafkaConsumer<String, SensorsSnapshotAvro> kafkaConsumer;

    public ConsumerRecords<String, SensorsSnapshotAvro> poll() {
        return kafkaConsumer.poll(Duration.ofMillis(1000));
    }

    public void commit() {
        kafkaConsumer.commitAsync();
    }

    public void close() {
        kafkaConsumer.close();
    }
}
