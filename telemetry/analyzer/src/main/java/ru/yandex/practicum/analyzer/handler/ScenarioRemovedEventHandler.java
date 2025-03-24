package ru.yandex.practicum.analyzer.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;


@Slf4j
@Service
@RequiredArgsConstructor
public class ScenarioRemovedEventHandler implements HubEventHandler {

    private final ScenarioRepository scenarioRepository;

    @Override
    public String getEventType() {
        return ScenarioRemovedEventAvro.class.getName();
    }

    @Override
    public void handle(HubEventAvro event) {
        ScenarioRemovedEventAvro scenarioRemovedEvent = (ScenarioRemovedEventAvro) event.getPayload();

        scenarioRepository.deleteByName(scenarioRemovedEvent.getName());
        log.info("Из БД удален scenario с name  = {}", scenarioRemovedEvent.getName());
    }

}
