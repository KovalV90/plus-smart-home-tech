package ru.yandex.practicum.analyzer.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class AnalyzerKafkaConnectorConfig {

    private final KafkaProperties kafkaProperties;

    private final Consumer<String, SensorsSnapshotAvro> snapshotConsumer;
    private final Consumer<String, HubEventAvro> hubEventConsumer;


    @Bean
    public KafkaConnector analyzerKafkaConnector() {
        return new KafkaConnector() {

            @Override
            public Consumer<String, SensorsSnapshotAvro> getSnapshotConsumer() {
                return snapshotConsumer;
            }

            @Override
            public Consumer<String, HubEventAvro> getHubEventConsumer() {
                return hubEventConsumer;
            }

            @Override
            public void stop() {
                if (snapshotConsumer != null) {
                    snapshotConsumer.commitSync();
                    snapshotConsumer.close(Duration.ofSeconds(kafkaProperties.getCloseClientTimeoutSec()));
                }
                if (hubEventConsumer != null) {
                    hubEventConsumer.commitSync();
                    hubEventConsumer.close(Duration.ofSeconds(kafkaProperties.getCloseClientTimeoutSec()));
                }
            }
        };
    }
}
