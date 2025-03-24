package ru.yandex.practicum.analyzer.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.model.Sensor;
import ru.yandex.practicum.analyzer.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;


@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceAddedEventHandler implements HubEventHandler {

    private final SensorRepository sensorRepository;

    @Override
    public String getEventType() {
        return DeviceAddedEventAvro.class.getName();
    }

    @Override
    public void handle(HubEventAvro event) {
        DeviceAddedEventAvro deviceAddedEvent = (DeviceAddedEventAvro) event.getPayload();
        Sensor sensor = new Sensor();
        sensor.setId(deviceAddedEvent.getId());
        sensor.setHubId(event.getHubId());

        sensorRepository.save(sensor);
        log.info("Сохранение sensor: {}", sensor);
    }

}
