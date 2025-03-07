package ru.yandex.practicum.collector.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.collector.model.HubEvent;
import ru.yandex.practicum.collector.model.SensorEvent;
import ru.yandex.practicum.collector.service.EventProcessingService;


@RestController
@RequestMapping("/events")
@Tag(name = "events", description = "API для передачи событий от датчиков и хабов")
public class EventController {
    private final EventProcessingService eventProcessingService;

    public EventController(EventProcessingService eventProcessingService) {
        this.eventProcessingService = eventProcessingService;
    }

    @PostMapping("/sensors")
    @Operation(summary = "Обработчик событий датчиков", description = "Эндпоинт для обработки событий от датчиков")
    public ResponseEntity<Void> collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        eventProcessingService.processSensorEvent(event);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/hubs")
    @Operation(summary = "Обработчик событий хабов", description = "Эндпоинт для обработки событий от хаба")
    public ResponseEntity<Void> collectHubEvent(@Valid @RequestBody HubEvent event) {
        eventProcessingService.processHubEvent(event);
        return ResponseEntity.ok().build();
    }
}
