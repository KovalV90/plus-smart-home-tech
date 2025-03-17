package ru.yandex.practicum.grpc.echo;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EchoSender {
    private static final Logger log = LoggerFactory.getLogger(EchoSender.class);

    @GrpcClient("echo")
    private EchoServiceGrpc.EchoServiceBlockingStub echoService;

    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    public void echo() {
        log.info("Sending echo request");
        EchoResponse response = echoService.echo(EchoRequest.newBuilder().setMessage("Ping!").build());
        log.info("Received response: {}", response.getMessage());
    }
}
