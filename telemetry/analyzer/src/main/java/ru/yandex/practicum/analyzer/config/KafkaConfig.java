package ru.yandex.practicum.analyzer.config;


import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.yandex.practicum.analyzer.consumer.SensorsSnapshotDeserializer;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Configuration
public class KafkaConfig {
    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${kafka.topic.snapshots}")
    private String snapshotsTopic;

    @Value("${kafka.topic.hubEvents}")
    private String hubEventsTopic;

    @Primary
    @Bean
    public Properties consumerProps() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "analyzer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorsSnapshotDeserializer.class);
        return props;
    }

    @Bean
    public Consumer<String, SensorsSnapshotAvro> kafkaConsumer(Properties consumerProps) {
        return new KafkaConsumer<>(consumerProps);
    }

    @Bean
    public Consumer<String, HubEventProto> hubEventsConsumer(Properties consumerPropsForHubEvents) {
        return new KafkaConsumer<>(consumerPropsForHubEvents);
    }

}
