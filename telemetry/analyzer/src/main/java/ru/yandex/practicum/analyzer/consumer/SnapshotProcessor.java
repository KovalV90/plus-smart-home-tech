package ru.yandex.practicum.analyzer.consumer;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.service.ScenarioService;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.Collections;

@Component
@Slf4j
public class SnapshotProcessor {

    private final Consumer<String, SensorsSnapshotAvro> consumer;
    private final ScenarioService scenarioService;
    private final String snapshotsTopic;
    private volatile boolean running = true;

    public SnapshotProcessor(
            Consumer<String, SensorsSnapshotAvro> consumer,
            ScenarioService scenarioService,
            @Value("${kafka.topic.snapshots}") String snapshotsTopic
    ) {
        this.consumer = consumer;
        this.scenarioService = scenarioService;
        this.snapshotsTopic = snapshotsTopic;
    }

    public void start() {
        consumer.subscribe(Collections.singletonList(snapshotsTopic));
        log.info("SnapshotProcessor subscribed to {}", snapshotsTopic);
        try {
            while (running) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = consumer.poll(Duration.ofMillis(1000));
                records.forEach(record -> scenarioService.processSnapshot(record.value()));
                consumer.commitAsync();
            }
        } catch (WakeupException e) {
            log.info("WakeupException in SnapshotProcessor, stopping...");
        } finally {
            shutdown();
        }
    }

    public void shutdown() {
        running = false;
        log.info("Closing SnapshotProcessor consumer...");
        try {
            consumer.commitSync();
        } finally {
            consumer.close();
        }
    }
}
