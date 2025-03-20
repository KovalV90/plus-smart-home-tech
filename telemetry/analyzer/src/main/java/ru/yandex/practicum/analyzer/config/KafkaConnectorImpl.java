package ru.yandex.practicum.analyzer.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class KafkaConnectorImpl {

    private final KafkaConfig kafkaConfig;

    @Bean
    KafkaConnector getClient() {
        return new KafkaConnector() {

            private Consumer<String, SensorsSnapshotAvro> snapshotConsumer;
            private Consumer<String, HubEventAvro> hubEventConsumer;

            @Override
            public Consumer<String, SensorsSnapshotAvro> getSnapshotConsumer() {
                if (snapshotConsumer == null) {
                    initSnapshotConsumer();
                }
                return snapshotConsumer;
            }

            @Override
            public Consumer<String, HubEventAvro> getHubEventConsumer() {
                if (hubEventConsumer == null) {
                    initHubEventConsumer();
                }
                return hubEventConsumer;
            }

            @Override
            public void stop() {
                if (snapshotConsumer != null) {
                    snapshotConsumer.commitSync();
                    snapshotConsumer.close(Duration.ofSeconds(kafkaConfig.getCloseClientTimeoutSec()));
                }
                if (hubEventConsumer != null) {
                    hubEventConsumer.commitSync();
                    hubEventConsumer.close(Duration.ofSeconds(kafkaConfig.getCloseClientTimeoutSec()));
                }
            }

            private void initSnapshotConsumer() {
                Properties properties = new Properties();
                properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapAddress());
                properties.put(ConsumerConfig.CLIENT_ID_CONFIG, kafkaConfig.getSnapshotConsumer().getClientId());
                properties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConfig.getSnapshotConsumer().getGroupId());
                properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConfig.getSnapshotConsumer().getKeyDeserializer());
                properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConfig.getSnapshotConsumer().getValueDeserializer());
                snapshotConsumer = new KafkaConsumer<>(properties);
                snapshotConsumer.subscribe(kafkaConfig.getSnapshotConsumer().getTopics());
            }

            private void initHubEventConsumer() {
                Properties properties = new Properties();
                properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapAddress());
                properties.put(ConsumerConfig.CLIENT_ID_CONFIG, kafkaConfig.getHubConsumer().getClientId());
                properties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConfig.getHubConsumer().getGroupId());
                properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConfig.getHubConsumer().getKeyDeserializer());
                properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConfig.getHubConsumer().getValueDeserializer());
                hubEventConsumer = new KafkaConsumer<>(properties);
                hubEventConsumer.subscribe(kafkaConfig.getHubConsumer().getTopics());
            }

        };
    }
}