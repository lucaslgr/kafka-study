package br.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

class FraudDetectorService {
    public static void main(String[] args) {
        final String topic = "ecommerce_store_new_order";
        final var fraudDetectorService = new FraudDetectorService();
        try(final var kafkaService = new KafkaService(FraudDetectorService.class.getSimpleName(), topic, fraudDetectorService::parse)) {
            kafkaService.run();
        }
    }

    private void parse(ConsumerRecord<String, String> record) {
        System.out.println("-".repeat(100));
        System.out.println("Processing new order, checking for fraud");
        System.out.println("key: " + record.key());
        System.out.println("value: " + record.value());
        System.out.println("partition: " + record.partition());
        System.out.println("offset: " + record.offset());
        try {
            Thread.sleep(5000); //Simulando o processamento de fraude
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Order processada!");
    }
}
