package ru.yandex.practicum.analyzer.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class KafkaHubEventConsumerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public Consumer<String, HubEventAvro> hubEventConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapAddress());
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, kafkaProperties.getHubConsumer().getClientId());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getHubConsumer().getGroupId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getHubConsumer().getKeyDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getHubConsumer().getValueDeserializer());

        Consumer<String, HubEventAvro> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(kafkaProperties.getHubConsumer().getTopics());

        return consumer;
    }
}
