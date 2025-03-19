package ru.yandex.practicum.analyzer.service;

public interface SensorService {
    void addSensor(String sensorId, String hubId, int deviceType);

    void removeSensor(String sensorId, String hubId);
}
