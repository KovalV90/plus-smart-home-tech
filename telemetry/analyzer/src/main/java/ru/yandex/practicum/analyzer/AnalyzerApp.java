package ru.yandex.practicum.analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.yandex.practicum.analyzer.consumer.SnapshotProcessor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@SpringBootApplication
public class AnalyzerApp {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(AnalyzerApp.class, args);
        SnapshotProcessor processor = context.getBean(SnapshotProcessor.class);

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.execute(processor);
    }
}
