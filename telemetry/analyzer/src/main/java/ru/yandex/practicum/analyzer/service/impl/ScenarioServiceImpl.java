package ru.yandex.practicum.analyzer.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.model.Scenario;
import ru.yandex.practicum.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.analyzer.service.ScenarioService;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc.HubRouterControllerBlockingStub;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Optional;
import java.util.List;

@Service
@Slf4j
public class ScenarioServiceImpl implements ScenarioService {

    private final ScenarioRepository scenarioRepository;
    private final HubRouterControllerBlockingStub hubRouterClient;

    public ScenarioServiceImpl(ScenarioRepository scenarioRepository,
                               HubRouterControllerBlockingStub hubRouterClient) {
        this.scenarioRepository = scenarioRepository;
        this.hubRouterClient = hubRouterClient;
    }

    @Override
    public void processSnapshot(SensorsSnapshotAvro snapshot) {
        List<Scenario> scenarios = scenarioRepository.findByHubId(snapshot.getHubId());
        log.info("Обрабатываем снапшот для хаба {}", snapshot.getHubId());
    }

    @Override
    public void addScenario(String hubId, ScenarioAddedEventProto scenarioAddedEvent) {
        Scenario scenario = new Scenario();
        scenario.setHubId(hubId);
        scenario.setName(scenarioAddedEvent.getName());
        scenarioRepository.save(scenario);
        log.info("Сценарий '{}' для хаба '{}' успешно добавлен.", scenario.getName(), hubId);
    }

    @Override
    public void removeScenario(String hubId, String scenarioName) {
        Optional<Scenario> scenarioOpt = scenarioRepository.findByHubIdAndName(hubId, scenarioName);
        if (scenarioOpt.isPresent()) {
            scenarioRepository.delete(scenarioOpt.get());
            log.info("Сценарий '{}' для хаба '{}' удалён.", scenarioName, hubId);
        } else {
            log.warn("Сценарий '{}' для хаба '{}' не найден.", scenarioName, hubId);
        }
    }
}
