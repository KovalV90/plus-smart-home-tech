package ru.yandex.practicum.analyzer.service;

import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface ScenarioService {

    /**
     * Обрабатывает снапшот состояния сенсоров для хаба.
     *
     * @param snapshot снапшот состояния сенсоров
     */
    void processSnapshot(SensorsSnapshotAvro snapshot);

    /**
     * Добавляет новый сценарий для указанного хаба.
     *
     * @param hubId идентификатор хаба
     * @param scenarioAddedEvent событие добавления сценария
     */
    void addScenario(String hubId, ScenarioAddedEventProto scenarioAddedEvent);

    /**
     * Удаляет сценарий по имени для указанного хаба.
     *
     * @param hubId идентификатор хаба
     * @param scenarioName название сценария
     */
    void removeScenario(String hubId, String scenarioName);
}
