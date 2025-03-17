package ru.yandex.practicum.grpc.echo;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class EchoServiceImpl extends EchoServiceGrpc.EchoServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(EchoServiceImpl.class);

    @Override
    public void echo(EchoRequest request, StreamObserver<EchoResponse> responseObserver) {
        try {
            log.info("Received echo request: {}", request.getMessage());

            // Формируем ответ
            EchoResponse response = EchoResponse.newBuilder()
                    .setMessage("Pong: " + request.getMessage())
                    .build();

            // Отправляем ответ клиенту
            responseObserver.onNext(response);
            responseObserver.onCompleted();

            log.info("Sent echo response: {}", response.getMessage());
        } catch (Exception e) {
            log.error("Error processing echo request", e);
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL.withDescription("Internal server error").withCause(e)
            ));
        }
    }
}
