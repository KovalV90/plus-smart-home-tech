package ru.yandex.practicum.analyzer.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.config.KafkaConnector;
import ru.yandex.practicum.analyzer.config.KafkaConfig;
import ru.yandex.practicum.analyzer.handler.SnapshotHandler;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class SnapshotProcessor implements Runnable {

    private final KafkaConnector kafkaConnector;
    private final KafkaConfig kafkaConfig;
    private final SnapshotHandler snapshotHandler;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    @Override
    public void run() {
        try (Consumer<String, SensorsSnapshotAvro> snapshotConsumer = kafkaConnector.getSnapshotConsumer()) {

            while (true) {

                Long pollTimeout = kafkaConfig.getSnapshotConsumer().getPollTimeoutSec();
                ConsumerRecords<String, SensorsSnapshotAvro> records = snapshotConsumer.poll(Duration.ofMillis(pollTimeout));

                int count = 0;
                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    handleRecord(record.value());
                    manageOffsets(record, count, snapshotConsumer);
                    count++;
                }
                snapshotConsumer.commitAsync();
            }

        } catch (Exception e) {
            log.error("Ошибка обработки снапшота в analyzer", e);
        }
    }

    private void handleRecord(SensorsSnapshotAvro sensorsSnapshot) {
        log.info("Обработка SensorSnapshot: {}", sensorsSnapshot);
        snapshotHandler.handle(sensorsSnapshot);
        log.info("Завершена обработка SensorSnapshot: {}", sensorsSnapshot);
    }

    private void manageOffsets(ConsumerRecord<String, SensorsSnapshotAvro> record, int count, Consumer<String, SensorsSnapshotAvro> consumer) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );
        if (count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Ошибка оффсетов в SnapshotProcessor: {}", offsets, exception);
                }
            });
        }
    }

}
