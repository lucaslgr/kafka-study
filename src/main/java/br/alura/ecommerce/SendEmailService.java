package br.alura.ecommerce;

import java.util.HashMap;
import org.apache.kafka.clients.consumer.ConsumerRecord;

class SendEmailService {
    public static void main(String[] args) {
        final var topic = "ecommerce_send_email";
        final var emailService = new SendEmailService();
        try(var kafkaService = new KafkaService(
                SendEmailService.class.getSimpleName(),
                topic,
                emailService::parse,
                String.class,
                new HashMap<>())) {
            kafkaService.run();
        }
    }

    private void parse(ConsumerRecord<String, String> record) {
        System.out.println("-".repeat(100));
        System.out.println("Send email");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        try {
            Thread.sleep(10); //Simulando o processamento de fraude
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Email enviado!");
    }
}
