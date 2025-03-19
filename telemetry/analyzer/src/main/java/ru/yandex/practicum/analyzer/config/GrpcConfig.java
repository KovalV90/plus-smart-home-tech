package ru.yandex.practicum.analyzer.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc.HubRouterControllerBlockingStub;

@Configuration
public class GrpcConfig {

    @Value("${grpc.client.hub-router.address}")
    private String hubRouterAddress;

    @Bean
    public ManagedChannel hubRouterChannel() {

        String target = hubRouterAddress.startsWith("static://")
                ? hubRouterAddress.substring("static://".length())
                : hubRouterAddress;
        return ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .build();
    }

    @Bean
    public HubRouterControllerBlockingStub hubRouterClient(ManagedChannel hubRouterChannel) {
        return HubRouterControllerGrpc.newBlockingStub(hubRouterChannel);
    }
}
