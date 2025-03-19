package ru.yandex.practicum.aggregator.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.aggregator.consumer.SensorEventDeserializer;
import ru.yandex.practicum.aggregator.producer.SnapshotSerializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Configuration
public class KafkaConfig {
    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${kafka.topic.sensors}")
    private String inputTopic;

    @Value("${kafka.topic.snapshots}")
    private String outputTopic;

    @Value("${kafka.consumer.group}")
    private String consumerGroup;

    @Bean
    public Properties consumerProps() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorEventDeserializer.class);

        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);

        return props;
    }

    @Bean
    public Properties producerProps() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SnapshotSerializer.class);

        return props;
    }
}
