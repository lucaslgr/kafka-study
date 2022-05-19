package br.alura.ecommerce;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

class KafkaService<T> implements Closeable {

    private final KafkaConsumer<String, T> consumer;
    private final ConsumerFunction parse;

    KafkaService(String groupId, String topic, ConsumerFunction parse, Class<T> classType, Map<String, String> overrideProperties) {
        this(groupId, parse, classType, overrideProperties);
        consumer.subscribe(Collections.singletonList(topic));
    }

    KafkaService(String groupId, Pattern topicPattern, ConsumerFunction parse, Class<T> classType,  Map<String, String> overrideProperties) {
        this(groupId, parse, classType, overrideProperties);
        consumer.subscribe(topicPattern);
    }

    private KafkaService(String groupId, ConsumerFunction parse, Class<T> classType, Map<String, String> overrideProperties) {
        this.parse = parse;
        this.consumer = new KafkaConsumer<>(getProperties(classType, groupId, overrideProperties));
    }

    void run() {
        while (true) {
            //Espera por 100mls para ver se chegou ou vai chegar uma msg
            var records = consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                for (var record : records) {
                    parse.consume(record);
                }
            }
        }
    }

    private Properties getProperties(Class<T> classType, String groupId, Map<String, String> overrideProperties) {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        //Colocando o nome da própria classe do consumer como grupo consumidor
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        properties.setProperty(GsonDeserializer.TYPE_CLASS_CONFIG, classType.getName());
        properties.putAll(overrideProperties);
        return properties;
    }

    @Override public void close() {
        consumer.close();
    }
}
