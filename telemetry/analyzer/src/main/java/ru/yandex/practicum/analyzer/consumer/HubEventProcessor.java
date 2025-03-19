package ru.yandex.practicum.analyzer.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.service.ScenarioService;
import ru.yandex.practicum.analyzer.service.SensorService;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;


import java.time.Duration;
import java.util.Collections;


@Component
@Slf4j
public class HubEventProcessor implements Runnable {

    private final Consumer<String, HubEventProto> consumer;
    private final ScenarioService scenarioService;
    private final SensorService sensorService;
    private final String hubEventsTopic;
    private volatile boolean running = true;

    public HubEventProcessor(
            Consumer<String, HubEventProto> hubEventsConsumer,
            ScenarioService scenarioService,
            SensorService sensorService,
            @Value("${kafka.topic.hubEvents}") String hubEventsTopic
    ) {
        this.consumer = hubEventsConsumer;
        this.scenarioService = scenarioService;
        this.sensorService = sensorService;
        this.hubEventsTopic = hubEventsTopic;
    }

    @Override
    public void run() {
        log.info("HubEventProcessor стартовал, topic={}", hubEventsTopic);
        consumer.subscribe(Collections.singletonList(hubEventsTopic));

        try {
            while (running) {

                ConsumerRecords<String, HubEventProto> records = consumer.poll(Duration.ofMillis(1000));
                records.forEach(record -> {
                    HubEventProto event = record.value();
                    processHubEvent(event);
                });

                consumer.commitAsync();
            }
        } catch (WakeupException e) {
            log.info("WakeupException полуено, остановлен HubEventProcessor...");
        } finally {
            shutdown();
        }
    }

    private void processHubEvent(HubEventProto event) {
        log.info("HubEventProto: hubId={}...", event.getHubId());

        if (event.hasDeviceAdded()) {
            String deviceId = event.getDeviceAdded().getId();
            sensorService.addSensor(deviceId, event.getHubId(), event.getDeviceAdded().getTypeValue());
        } else if (event.hasDeviceRemoved()) {
            String deviceId = event.getDeviceRemoved().getId();
            sensorService.removeSensor(deviceId, event.getHubId());
        } else if (event.hasScenarioAdded()) {
            scenarioService.addScenario(event.getHubId(), event.getScenarioAdded());
        } else if (event.hasScenarioRemoved()) {
            scenarioService.removeScenario(event.getHubId(), event.getScenarioRemoved().getName());
        } else {
            log.warn("HubEventProto");
        }
    }


    public void shutdown() {
        running = false;
        log.info("Shutting down...");
        try {
            consumer.commitSync();
        } finally {
            consumer.close();
        }
    }
}
