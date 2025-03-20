package ru.yandex.practicum.collector.kafka;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;

public interface KafkaProducerManager {
    Producer<String, SpecificRecordBase> getProducer();

    void stop();
}
