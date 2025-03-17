package ru.yandex.practicum.aggregator.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Component
public class SensorEventConsumer {
    private final Consumer<String, SensorEventAvro> consumer;

    public SensorEventConsumer(Properties consumerProps) {
        this.consumer = new KafkaConsumer<>(consumerProps);
        this.consumer.subscribe(Collections.singletonList("telemetry.sensors.v1"));
    }

    public ConsumerRecords<String, SensorEventAvro> poll() {
        return consumer.poll(Duration.ofMillis(1000));
    }

    public void commit() {
        consumer.commitAsync();
    }

    public void close() {
        consumer.close();
    }
}
