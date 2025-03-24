package ru.yandex.practicum.aggregator.config;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class AggregatorKafkaConnectorConfig {

    @Value("${aggregator.kafka.closeClientTimeoutSec}")
    private Integer closeTimeoutSeconds;

    private final Producer<String, SpecificRecordBase> aggregatorProducer;
    private final Consumer<String, SensorEventAvro> aggregatorConsumer;

    @Bean
    public KafkaConnector aggregatorKafkaConnector() {
        return new KafkaConnector() {

            @Override
            public Producer<String, SpecificRecordBase> getProducer() {
                return aggregatorProducer;
            }

            @Override
            public Consumer<String, SensorEventAvro> getConsumer() {
                return aggregatorConsumer;
            }

            @Override
            public void stop() {
                if (aggregatorProducer != null) {
                    aggregatorProducer.flush();
                    aggregatorProducer.close(Duration.ofSeconds(closeTimeoutSeconds));
                }
                if (aggregatorConsumer != null) {
                    aggregatorConsumer.commitSync();
                    aggregatorConsumer.close(Duration.ofSeconds(closeTimeoutSeconds));
                }
            }
        };
    }
}
