package ru.yandex.practicum.analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.analyzer.consumer.HubEventProcessor;
import ru.yandex.practicum.analyzer.consumer.SnapshotProcessor;

@SpringBootApplication
public class AnalyzerApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AnalyzerApp.class, args);


        HubEventProcessor hubEventProcessor = context.getBean(HubEventProcessor.class);
        SnapshotProcessor snapshotProcessor = context.getBean(SnapshotProcessor.class);

        Thread hubEventsThread = new Thread(hubEventProcessor, "HubEventHandlerThread");
        hubEventsThread.start();


        snapshotProcessor.start();


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            hubEventProcessor.shutdown();
            snapshotProcessor.shutdown();
        }));
    }
}
