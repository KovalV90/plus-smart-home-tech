package ru.yandex.practicum.analyzer.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.common.serialization.Deserializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Map;

@Slf4j
public class SensorsSnapshotDeserializer implements Deserializer<SensorsSnapshotAvro> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public SensorsSnapshotAvro deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        SpecificDatumReader<SensorsSnapshotAvro> reader = new SpecificDatumReader<>(SensorsSnapshotAvro.getClassSchema());
        Decoder decoder = DecoderFactory.get().binaryDecoder(data, null);

        try {
            return reader.read(null, decoder);
        } catch (Exception e) {
            log.error("Ошибка десериализации SensorsSnapshotAvro", e);
            return null;
        }
    }

    @Override
    public void close() {
    }
}
