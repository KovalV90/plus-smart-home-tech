package ru.yandex.practicum.collector.converter;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;
import java.util.Map;

public class GeneralAvroSerializer<T extends SpecificRecordBase> implements Serializer<T> {
    private static final String AVRO_RECORD_CLASS_CONFIG = "avro.record.class";
    private Class<T> avroClass;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Object classNameObj = configs.get(AVRO_RECORD_CLASS_CONFIG);
        if (classNameObj instanceof String className) {
            try {
                @SuppressWarnings("unchecked")
                Class<T> clazz = (Class<T>) Class.forName(className);
                this.avroClass = clazz;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Не найден класс Avro записи: " + className, e);
            }
        }
    }

    @Override
    public byte[] serialize(String topic, T data) {
        if (data == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        Class<T> clazz = (this.avroClass != null) ? this.avroClass : (Class<T>) data.getClass();
        try {
            return AvroSerializationUtil.serialize(data, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сериализации Avro объекта", e);
        }
    }

    @Override
    public void close() {
    }
}
