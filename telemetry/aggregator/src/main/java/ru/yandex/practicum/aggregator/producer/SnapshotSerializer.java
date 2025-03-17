package ru.yandex.practicum.aggregator.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.serialization.Serializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class SnapshotSerializer implements Serializer<SensorsSnapshotAvro> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, SensorsSnapshotAvro data) {
        if (data == null) {
            return null;
        }

        SpecificDatumWriter<SensorsSnapshotAvro> writer = new SpecificDatumWriter<>(SensorsSnapshotAvro.getClassSchema());
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
            writer.write(data, encoder);
            encoder.flush();
            return out.toByteArray();
        } catch (IOException e) {
            log.error("Ошибка сериализации SensorsSnapshotAvro", e);
            return null;
        }
    }

    @Override
    public void close() {
    }
}
