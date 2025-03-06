package ru.yandex.practicum.collector.converter;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AvroSerializationUtil {

    public static <T> byte[] serialize(T avroObject, Class<T> clazz) throws IOException {
        SpecificDatumWriter<T> writer = new SpecificDatumWriter<>(clazz);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        writer.write(avroObject, encoder);
        encoder.flush();
        return out.toByteArray();
    }
}
