package br.alura.ecommerce;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

class KafkaDispatcher implements Closeable {
    private final KafkaProducer<String, String> producer;

    KafkaDispatcher() {
        producer = new KafkaProducer<String, String>(properties());
    }

    void send(String topic, String key, String value) throws ExecutionException, InterruptedException {
        final var producerRecord = new ProducerRecord<String, String>(topic, key, value);
        final Callback kafkaCallback = (data, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            }
            System.out.println("topic:" +
                    data.topic() +
                    ":::partition: " +
                    data.partition() +
                    "/ offset: " +
                    data.offset() +
                    "/ timestamp: " +
                    data.timestamp());
        };
        producer.send(producerRecord, kafkaCallback).get();
    }

    private static Properties properties() {
        var properties = new Properties();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return properties;
    }

    @Override public void close() {
        producer.close();
    }
}
