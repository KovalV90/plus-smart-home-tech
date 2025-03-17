package ru.yandex.practicum.collector;

import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FullCycleIntegrationTest {

    @Test
    public void testFullCycle() throws InterruptedException, IOException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 59091)
                .usePlaintext()
                .build();
        CollectorControllerGrpc.CollectorControllerBlockingStub stub =
                CollectorControllerGrpc.newBlockingStub(channel);

        long currentMillis = System.currentTimeMillis();
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(currentMillis / 1000)
                .setNanos((int) ((currentMillis % 1000) * 1_000_000))
                .build();

        DeviceAddedEventProto deviceAdded = DeviceAddedEventProto.newBuilder()
                .setId("test-device-123")
                .setType(DeviceTypeProto.MOTION_SENSOR)
                .build();

        HubEventProto hubEvent = HubEventProto.newBuilder()
                .setHubId("test-hub-1")
                .setTimestamp(timestamp)
                .setDeviceAdded(deviceAdded)
                .build();

        Empty response = stub.collectHubEvent(hubEvent);
        assertNotNull(response, "Ответ от gRPC не должен быть null");

        Thread.sleep(2000);

        channel.shutdownNow();

        Properties consumerProps = new Properties();
        consumerProps.put("bootstrap.servers", "localhost:9092");
        consumerProps.put("group.id", "full-cycle-test-group-" + UUID.randomUUID());
        consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        consumerProps.put("auto.offset.reset", "earliest");

        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singleton("telemetry.hubs.v1"));

        boolean found = false;
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 30000) {
            ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, byte[]> record : records) {
                HubEventAvro eventAvro = deserialize(record.value(), HubEventAvro.class);
                if ("test-hub-1".equals(eventAvro.getHubId())
                        && eventAvro.getPayload() instanceof DeviceAddedEventAvro) {
                    DeviceAddedEventAvro addedEvent = (DeviceAddedEventAvro) eventAvro.getPayload();
                    if ("test-device-123".equals(addedEvent.getId())) {
                        found = true;
                        break;
                    }
                }
            }
            if (found) break;
        }
        consumer.close();
        assertTrue(found, "Не удалось найти отправленное устройство в топике telemetry.hubs.v1");
    }

    private <T extends SpecificRecordBase> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        DatumReader<T> reader = new SpecificDatumReader<>(clazz);
        Decoder decoder = DecoderFactory.get().binaryDecoder(data, null);
        return reader.read(null, decoder);
    }
}
