package ru.yandex.practicum.analyzer.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.service.SnapshotHandler;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class SnapshotProcessor implements Runnable {

    private final KafkaConsumer<String, SensorsSnapshotAvro> consumer;
    private final SnapshotHandler snapshotHandler;
    private final String topic;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public SnapshotProcessor(KafkaConsumer<String, SensorsSnapshotAvro> consumer,
                             SnapshotHandler snapshotHandler) {
        this.consumer = consumer;
        this.snapshotHandler = snapshotHandler;
        this.topic = "telemetry.snapshots.v1";
    }

    @Override
    public void run() {
        consumer.subscribe(Collections.singletonList(topic));
        log.info("Подписан на топик: {}", topic);

        try {
            while (running.get()) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = consumer.poll(Duration.ofMillis(1000));

                if (!records.isEmpty()) {
                    log.info("Получено {} сообщений", records.count());

                    records.forEach(record -> {
                        try {
                            snapshotHandler.processSnapshot(record.value());


                            consumer.commitSync(Map.of(
                                    new TopicPartition(record.topic(), record.partition()),
                                    new OffsetAndMetadata(record.offset() + 1)
                            ));
                            log.info("Offset зафиксирован: partition={}, offset={}", record.partition(), record.offset());
                        } catch (Exception e) {
                            log.error("Ошибка обработки снапшота: {}", record.value(), e);
                        }
                    });
                } else {
                    log.debug("Нет новых сообщений, жду...");
                    Thread.sleep(500);
                }
            }
        } catch (Exception e) {
            log.error("Ошибка в SnapshotProcessor", e);
        } finally {
            shutdown();
        }
    }

    public void shutdown() {
        running.set(false);
        log.info("Остановка SnapshotProcessor...");
        try {
            consumer.commitSync();
        } finally {
            consumer.close();
            log.info("Kafka Consumer закрыт");
        }
    }
}
