package ru.yandex.practicum.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.analyzer.model.ScenarioActionLink;
import ru.yandex.practicum.analyzer.model.ScenarioActionLinkId;

public interface ScenarioActionLinkRepository
        extends JpaRepository<ScenarioActionLink, ScenarioActionLinkId> {
}
