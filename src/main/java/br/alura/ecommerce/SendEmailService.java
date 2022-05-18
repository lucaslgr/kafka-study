package br.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

class SendEmailService {
    public static void main(String[] args) {
        final var topic = "ecommerce_send_email";
        final var emailService = new SendEmailService();
        var kafkaService = new KafkaService(SendEmailService.class.getSimpleName(), topic, emailService::parse);
        kafkaService.run();

    }

    private void parse(ConsumerRecord<String, String> record) {
        System.out.println("-".repeat(100));
        System.out.println("Sending the email for this order");
        System.out.println("key: " + record.key());
        System.out.println("value: " + record.value());
        System.out.println("partition: " + record.partition());
        System.out.println("offset: " + record.offset());
        try {
            Thread.sleep(5000); //Simulando o processamento de fraude
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Email enviado!");
    }
}
