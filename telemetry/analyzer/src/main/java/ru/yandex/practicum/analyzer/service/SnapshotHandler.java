package ru.yandex.practicum.analyzer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.producer.SnapshotProducer;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SnapshotHandler {

    private final SnapshotProducer snapshotProducer;
    private final ConcurrentHashMap<String, SensorsSnapshotAvro> lastSnapshots = new ConcurrentHashMap<>();

    public SnapshotHandler(SnapshotProducer snapshotProducer) {
        this.snapshotProducer = snapshotProducer;
    }

    public void processSnapshot(SensorsSnapshotAvro snapshot) {
        if (snapshot == null || snapshot.getHubId() == null) {
            log.warn("Пропущен некорректный снапшот: {}", snapshot);
            return;
        }

        log.info("Обработка снапшота для хаба {}: {}", snapshot.getHubId(), snapshot);

        SensorsSnapshotAvro lastSnapshot = lastSnapshots.get(snapshot.getHubId());

        if (lastSnapshot == null || !lastSnapshot.equals(snapshot)) {
            snapshotProducer.sendSnapshot(snapshot);
            lastSnapshots.put(snapshot.getHubId(), snapshot);
            log.info("Новый снапшот отправлен: {}", snapshot);
        } else {
            log.info("Дублирующий снапшот, отправка пропущена.");
        }
    }
}
