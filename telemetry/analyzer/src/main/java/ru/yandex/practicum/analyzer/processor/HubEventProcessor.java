package ru.yandex.practicum.analyzer.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.config.KafkaConnector;
import ru.yandex.practicum.analyzer.config.KafkaConfig;
import ru.yandex.practicum.analyzer.handler.HubEventHandler;
import ru.yandex.practicum.analyzer.repository.ActionRepository;
import ru.yandex.practicum.analyzer.repository.ConditionRepository;
import ru.yandex.practicum.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.analyzer.repository.SensorRepository;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;


import java.time.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
@Slf4j
public class HubEventProcessor implements Runnable {

    private final KafkaConnector kafkaConnector;
    private final KafkaConfig kafkaConfig;

    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    private final Map<String, HubEventHandler> hubEventHandlers;

    public HubEventProcessor(KafkaConnector kafkaConnector,
                             KafkaConfig kafkaConfig,
                             ActionRepository actionRepository,
                             ConditionRepository conditionRepository,
                             ScenarioRepository scenarioRepository,
                             SensorRepository sensorRepository,
                             Set<HubEventHandler> hubEventHandlers) {
        this.kafkaConnector = kafkaConnector;
        this.kafkaConfig = kafkaConfig;
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(
                        HubEventHandler::getEventType,
                        Function.identity()
                ));
        log.info("Создан hubEventHandlers: {}", hubEventHandlers);
    }

    @Override
    public void run() {
        log.info("Запущен HubEventProcessor");
        try (Consumer<String, HubEventAvro> hubEventConsumer = kafkaConnector.getHubEventConsumer()) {
            while (true) {
                Long pollTimeout = kafkaConfig.getHubConsumer().getPollTimeoutSec();
                ConsumerRecords<String, HubEventAvro> records = hubEventConsumer.poll(Duration.ofMillis(pollTimeout));

                int count = 0;
                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    handleRecord(record.value());
                    manageOffsets(record, count, hubEventConsumer);
                    count++;
                }
                hubEventConsumer.commitAsync();
            }
        } catch (Exception e) {
            log.error("Ошибка обработки HubEventAvro в analyzer", e);
        }
    }

    private void manageOffsets(ConsumerRecord<String, HubEventAvro> record, int count, Consumer<String, HubEventAvro> consumer) {
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

    private void handleRecord(HubEventAvro hubEventAvro) {
        log.info("Обработка события: {}", hubEventAvro);
        String eventType = hubEventAvro.getPayload().getClass().getName();
        if (hubEventHandlers.containsKey(eventType)) {
            log.info("Обработчик: {}", hubEventHandlers.get(eventType));
            hubEventHandlers.get(eventType).handle(hubEventAvro);
        } else {
            throw new IllegalArgumentException("Не найден обработчик события: " + eventType);
        }
    }
}
