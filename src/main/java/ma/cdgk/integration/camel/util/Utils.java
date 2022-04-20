package ma.cdgk.integration.camel.util;

import io.cloudevents.CloudEventData;
import io.cloudevents.core.data.BytesCloudEventData;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.cdgk.integration.common.QueueTopicPair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Utils {

    public static final String XML_FORMAT = "xml";
    public static final String JSON_FORMAT = "json";
    public static final String KAFKA_TOPIC_HEADER_NAME = "kafka.TOPIC";
    public static final String JMS_DELIVERY_MODE = "JMSDeliveryMode";

    @AllArgsConstructor
    @Getter
    public enum TopicFormat {
        CLOUD_EVENT("CloudEvent"),
        JSON("Json");
        private String formatName;
    }

    @AllArgsConstructor
    @Getter
    public enum Routes {
        KAFKA("kafka"),
        ACTIVE_MQ("activemq");
        private String routeName;
    }

    public static Object deserializeCloudEventData(
            String topic, CloudEventData cloudEventData, String schemaRegistryUrl) {
        BytesCloudEventData bytesCloudEventData = (BytesCloudEventData) cloudEventData;
        Map<String, Object> config = new HashMap<>();
        config.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        config.put(AbstractKafkaAvroSerDeConfig.AUTO_REGISTER_SCHEMAS, true);
        KafkaAvroDeserializer avroDeserializer = new KafkaAvroDeserializer();
        avroDeserializer.configure(config, false);
        return avroDeserializer.deserialize(topic, bytesCloudEventData.toBytes());
    }

    public static byte[] serializeCloudEventData(
            Object item, String schemaRegistryUrl, String topic) {
        Map<String, Object> config = new HashMap<>();
        config.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        config.put(AbstractKafkaAvroSerDeConfig.AUTO_REGISTER_SCHEMAS, true);
        KafkaAvroSerializer avroSerializer = new KafkaAvroSerializer();
        avroSerializer.configure(config, false);
        return avroSerializer.serialize(topic, item);
    }

    public static QueueTopicPair getQueueTopicPairFromConfig(
            List<QueueTopicPair> lists, Predicate<QueueTopicPair> predicate) {
        return lists.stream().filter(predicate).findFirst().orElse(null);
    }
}
