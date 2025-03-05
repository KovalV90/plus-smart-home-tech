package telemetry.collector.kafka;

import telemetry.collector.converter.AvroConverter;
import telemetry.collector.model.SensorEvent;
import telemetry.collector.model.HubEvent;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.stereotype.Service;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KafkaCollectorProducer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaCollectorProducer.class);
    private final KafkaProducer<String, byte[]> producer;

    public KafkaCollectorProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", StringSerializer.class.getName());
        // Используем сериализацию в бинарный формат
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
}
