package br.alura.ecommerce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (final var orderKafkaDispatcher = new KafkaDispatcher<Order>()) {
            try (final var emailKafkaDispatcher = new KafkaDispatcher<Email>()) {
                for (var i = 0; i < 100; i++) {
                    final var userId = UUID.randomUUID().toString();
                    final var orderId = UUID.randomUUID().toString();
                    final var amount = new BigDecimal(Math.random() * 5000 + 1);

                    final var order = new Order(userId, orderId, amount);
                    orderKafkaDispatcher.send("ecommerce_store_new_order", userId, order);

                    var email = new Email("Email para teste", "Esse email é apenas para teste");
                    emailKafkaDispatcher.send("ecommerce_send_email", userId, email);
                }
            }
        }

    }


}
