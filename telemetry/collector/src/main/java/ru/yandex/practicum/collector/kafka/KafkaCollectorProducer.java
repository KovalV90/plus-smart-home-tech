package ru.yandex.practicum.collector.kafka;

import jakarta.annotation.PreDestroy;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.collector.converter.AvroConverter;
import ru.yandex.practicum.collector.model.HubEvent;
import ru.yandex.practicum.collector.model.SensorEvent;

import java.util.Properties;

@Service
public class KafkaCollectorProducer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaCollectorProducer.class);
    private final KafkaProducer<String, byte[]> producer;

    public KafkaCollectorProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", ByteArraySerializer.class.getName());
        producer = new KafkaProducer<>(props);
    }

    public void sendSensorEvent(String topic, SensorEvent event) {
        try {
            byte[] avroBytes = AvroConverter.convertSensorEvent(event);
            ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, event.getId(), avroBytes);
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    logger.error("Ошибка отправки сенсорного события в Kafka", exception);
                } else {
                    logger.info("Сенсорное событие отправлено в Kafka: topic={}, partition={}, offset={}",
                            metadata.topic(), metadata.partition(), metadata.offset());
                }
            });
        } catch (Exception e) {
            logger.error("Не удалось сконвертировать сенсорное событие в Avro", e);
        }
    }

    public void sendHubEvent(String topic, HubEvent event) {
        try {
            logger.info("Попытка отправить HubEvent в Kafka: hubId={}, type={}",
                    event.getHubId(), event.getClass().getSimpleName());
            byte[] avroBytes = AvroConverter.convertHubEvent(event);
            ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, event.getHubId(), avroBytes);
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    logger.error("Ошибка отправки события хаба в Kafka", exception);
                } else {

                    logger.info("Событие хаба отправлено в Kafka: topic={}, partition={}, offset={}",
                            metadata.topic(), metadata.partition(), metadata.offset());
                }
            });
        } catch (Exception e) {
            logger.error("Не удалось сконвертировать событие хаба в Avro", e);
        }
    }

    @PreDestroy
    public void closeProducer() {
        logger.info("Закрытие KafkaProducer...");
        producer.close();
        logger.info("KafkaProducer закрыт.");
    }
}

