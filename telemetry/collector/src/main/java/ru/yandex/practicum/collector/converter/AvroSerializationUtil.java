package ru.yandex.practicum.collector.converter;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AvroSerializationUtil {

    public static <T> byte[] serialize(T avroObject, Class<T> clazz) throws IOException {
        if (avroObject == null) {
            throw new IllegalArgumentException("avroObject не может быть null");
        }
        SpecificDatumWriter<T> writer = new SpecificDatumWriter<>(clazz);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
            writer.write(avroObject, encoder);
            encoder.flush();
            return out.toByteArray();
        }
    }
}
