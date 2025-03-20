package ru.yandex.practicum.collector.converter;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Slf4j
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
                log.error("Не найден класс Avro записи: {}", className, e);

                throw new RuntimeException("Не найден класс Avro записи: " + className, e);
            }
        }
    }

    @Override
    public byte[] serialize(String topic, SpecificRecordBase data) {
        if (data == null) {
            log.warn("Передан null в сериализатор Avro, topic={}", topic);
            return null;
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
            DatumWriter<SpecificRecordBase> writer = new SpecificDatumWriter<>(data.getSchema());

            writer.write(data, encoder);
            encoder.flush();

            return out.toByteArray();
        } catch (IOException ex) {
            log.error("Ошибка сериализации Avro объекта для топика [{}]: {}", topic, ex.getMessage(), ex);
            throw new SerializationException("Ошибка сериализации Avro объекта для топика [" + topic + "]", ex);
        }
    }


    @Override
    public void close() {
    }
}
