package br.alura.ecommerce;

import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonDeserializer<T> implements Deserializer<T> {

    public static final String TYPE_CLASS_CONFIG = "bt.com.alura.ecommerce.type_config_class";
    private final Gson gson = new GsonBuilder().create();
    private Class<T> classType;

    @Override public void configure(Map<String, ?> configs, boolean isKey) {
        final String typeName = String.valueOf(configs.get(TYPE_CLASS_CONFIG));
        try {
            classType = (Class<T>) Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Type for deserialization does not exist in the class path: ", e);
        }
    }

    @Override public T deserialize(String topic, byte[] data) {
        return gson.fromJson(new String(data), classType);
    }
}
