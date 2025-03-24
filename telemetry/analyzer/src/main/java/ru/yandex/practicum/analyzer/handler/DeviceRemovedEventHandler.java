package ru.yandex.practicum.analyzer.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;


@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceRemovedEventHandler implements HubEventHandler {

    private final SensorRepository sensorRepository;

    @Override
    public String getEventType() {
        return DeviceRemovedEventAvro.class.getName();
    }

    @Override
    public void handle(HubEventAvro event) {
        DeviceRemovedEventAvro deviceRemovedEvent = (DeviceRemovedEventAvro) event.getPayload();

        sensorRepository.deleteById(deviceRemovedEvent.getId());
        log.info("Удаление sensor с id  = {}", deviceRemovedEvent.getId());
    }

}
