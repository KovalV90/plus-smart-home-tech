package ru.yandex.practicum.collector.grpc;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.collector.handler.HubEventHandler;
import ru.yandex.practicum.collector.handler.SensorEventHandler;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.*;

import java.util.List;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class EventGrpcService extends CollectorControllerGrpc.CollectorControllerImplBase {

    private final List<HubEventHandler> hubEventHandlers;
    private final List<SensorEventHandler> sensorEventHandlers;

    @PostConstruct
    private void init() {
        log.info("Инициализирован EventGrpcService с {} hubEventHandlers и {} sensorEventHandlers",
                hubEventHandlers.size(), sensorEventHandlers.size());
    }

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        log.info("Получено событие от хаба: {}", request);

        boolean handled = hubEventHandlers.stream()
                .filter(handler -> handler.getMessageType() == request.getPayloadCase())
                .findFirst()
                .map(handler -> {
                    handler.handle(request);
                    return true;
                })
                .orElse(false);

        if (!handled) {
            log.warn("Не найден обработчик для события: {}", request.getPayloadCase());
        }

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        log.info("Получено событие от сенсора: {}", request);

        boolean handled = sensorEventHandlers.stream()
                .filter(handler -> handler.getMessageType() == request.getPayloadCase())
                .findFirst()
                .map(handler -> {
                    handler.handle(request);
                    return true;
                })
                .orElse(false);

        if (!handled) {
            log.warn("Не найден обработчик для события: {}", request.getPayloadCase());
        }

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
