package br.alura.ecommerce;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (final var kafkaDispatcher = new KafkaDispatcher()) {
            for (var i = 0; i < 100; i++) {
                var key = UUID.randomUUID().toString();
                var value = key + ",111,111,111";

                kafkaDispatcher.send("ecommerce_store_new_order", key, value);

                var email = "lucaslgr543@gmail.com";
                kafkaDispatcher.send("ecommerce_send_email", key, email);
            }
        }

    }


}
