package ru.yandex.practicum.aggregator.config;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public interface KafkaConnector {

    Producer<String, SpecificRecordBase> getProducer();

    Consumer<String, SensorEventAvro> getConsumer();

    void stop();

}
