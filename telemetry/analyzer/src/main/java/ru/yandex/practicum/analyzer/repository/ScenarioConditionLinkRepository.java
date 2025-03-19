package ru.yandex.practicum.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.analyzer.model.ScenarioConditionLink;
import ru.yandex.practicum.analyzer.model.ScenarioConditionLinkId;

public interface ScenarioConditionLinkRepository
        extends JpaRepository<ScenarioConditionLink, ScenarioConditionLinkId> {
}
