package br.alura.ecommerce;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

public class LogService {
    public static void main(String[] args) {
        final var logService = new LogService();
        try (final var kafkaService = new KafkaService<String>(
                LogService.class.getSimpleName(),
                Pattern.compile("ecommerce.*"),
                logService::parse,
                String.class,
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()))) {
            kafkaService.run();
        }
    }

    private void parse(ConsumerRecord<String, String> record) {
        System.out.println("-".repeat(100));
        System.out.println("Log# topic: " + record.topic());
        System.out.println("key: " + record.key());
        System.out.println("value: " + record.value());
        System.out.println("partition: " + record.partition());
        System.out.println("offset: " + record.offset());
        System.out.println("Order logged!");
        try {
            Thread.sleep(500); //Simulando o processamento de fraude
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
