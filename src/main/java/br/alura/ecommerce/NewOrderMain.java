package br.alura.ecommerce;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var producer = new KafkaProducer<String, String>(properties());

        Callback kafkaCallback = (data, exception) -> {
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

        for (var i = 0; i < 100; i++) {
            var key = UUID.randomUUID().toString();
            var value = key+",111,111,111";

            var recordStoreNewOrder = new ProducerRecord<String, String>("ecommerce_store_new_order", key, value);
            producer.send(recordStoreNewOrder, kafkaCallback).get();

            var email = "lucaslgr543@gmail.com";
            var recordSendEmail = new ProducerRecord<String, String>("ecommerce_send_email", key, email);
            producer.send(recordSendEmail, kafkaCallback).get();
        }
    }

    private static Properties properties() {
        var properties = new Properties();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return properties;
    }
}
