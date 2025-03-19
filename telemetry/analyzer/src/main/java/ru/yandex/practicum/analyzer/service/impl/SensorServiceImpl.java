package ru.yandex.practicum.analyzer.service.impl;

import ru.yandex.practicum.analyzer.service.SensorService;
import org.springframework.stereotype.Service;

@Service
public class SensorServiceImpl implements SensorService {

    @Override
    public void addSensor(String sensorId, String hubId, int deviceType) {
        System.out.println("Добавление сенсора: " + sensorId + " для хаба: " + hubId);
    }

    @Override
    public void removeSensor(String sensorId, String hubId) {
        System.out.println("Удаление сенсора: " + sensorId + " для хаба: " + hubId);
    }
}
