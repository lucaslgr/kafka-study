package br.alura.ecommerce;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

class FraudDetectorService {
    public static void main(String[] args) {
        final String topic = "ecommerce_store_new_order";
        final var fraudDetectorService = new FraudDetectorService();
        try(final var kafkaService = new KafkaService<Order>(
                FraudDetectorService.class.getSimpleName(),
                topic,
                fraudDetectorService::parse,
                Order.class,
                new HashMap<>()
        )) {
            kafkaService.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> record) {
        System.out.println("-".repeat(100));
        System.out.println("Processing new order, checking for fraud");
        System.out.println("key: " + record.key());
        System.out.println("value: " + record.value());
        System.out.println("partition: " + record.partition());
        System.out.println("offset: " + record.offset());
        try {
            Thread.sleep(500); //Simulando o processamento de fraude
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Order processada!");
    }
}
