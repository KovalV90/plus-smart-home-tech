package ru.yandex.practicum.analyzer.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class KafkaSnapshotConsumerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public Consumer<String, SensorsSnapshotAvro> snapshotConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapAddress());
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, kafkaProperties.getSnapshotConsumer().getClientId());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getSnapshotConsumer().getGroupId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getSnapshotConsumer().getKeyDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getSnapshotConsumer().getValueDeserializer());

        Consumer<String, SensorsSnapshotAvro> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(kafkaProperties.getSnapshotConsumer().getTopics());

        return consumer;
    }
}
