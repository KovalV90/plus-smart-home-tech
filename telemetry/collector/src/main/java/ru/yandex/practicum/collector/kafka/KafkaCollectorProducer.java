package ru.yandex.practicum.collector.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.collector.converter.GeneralAvroSerializer;

import java.time.Instant;
import java.util.Properties;

@Slf4j
@Service
public class KafkaCollectorProducer implements AutoCloseable {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    //private static final String KEY_SERIALIZER = StringSerializer.class.getName();
    private static final Class<?> KEY_SERIALIZER = StringSerializer.class;
    // private static final String VALUE_SERIALIZER = "ru.yandex.practicum.collector.converter.GeneralAvroSerializer";
    private static final Class<?> VALUE_SERIALIZER = GeneralAvroSerializer.class;

    private final KafkaProducer<String, SpecificRecordBase> producer;

    public KafkaCollectorProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        props.put("key.serializer", KEY_SERIALIZER.getName());
        props.put("value.serializer", VALUE_SERIALIZER.getName());
        producer = new KafkaProducer<>(props);
    }

    /**
     * Обрабатывает событие от датчика и сохраняет его в топике Kafka.
     *
     * @param event     Событие от датчика
     * @param hubId     Идентификатор хаба, в котором зарегистрирован датчик
     * @param timestamp Метка времени, когда произошло событие
     * @param topicType Тип топика который нужно использовать для отправки сообщения
     */
    public void send(SpecificRecordBase event, String hubId, Instant timestamp, TopicType topicType) {
        if (event == null || hubId == null || timestamp == null || topicType == null) {
            log.error("Некорректные входные параметры для отправки сообщения в Kafka");
            return;
        }
        log.debug("Отправка сообщения: hubId={}, timestamp={}", hubId, timestamp.toEpochMilli());

        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                topicType.getTopicName(),
                null,
                timestamp.toEpochMilli(),
                hubId,
                event
        );
        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                log.error("Ошибка отправки события в Kafka", exception);
            } else {
                log.info("Сообщение отправлено в Kafka: topic={}, partition={}, offset={}",
                        metadata.topic(), metadata.partition(), metadata.offset());
            }
        });
    }

    @Override
    public void close() {
        log.info("Флаш и закрытие KafkaProducer...");
        producer.flush();
        producer.close();
        log.info("KafkaProducer закрыт.");
    }
}

