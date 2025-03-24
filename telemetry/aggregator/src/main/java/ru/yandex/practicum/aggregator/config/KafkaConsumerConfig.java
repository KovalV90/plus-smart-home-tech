package ru.yandex.practicum.aggregator.config;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.deserializer.SensorEventDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.util.Properties;

@Configuration
public class KafkaConsumerConfig {

    @Value("${kafkaServer}")
    private String bootstrapAddress;

    @Value("${aggregator.kafka.consumer.id}")
    private String consumerId;

    @Value("${aggregator.kafka.consumer.group}")
    private String consumerGroupId;

    @Bean
    public Consumer<String, SensorEventAvro> aggregatorConsumer() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, consumerId);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorEventDeserializer.class);

        return new KafkaConsumer<>(properties);
    }
}
