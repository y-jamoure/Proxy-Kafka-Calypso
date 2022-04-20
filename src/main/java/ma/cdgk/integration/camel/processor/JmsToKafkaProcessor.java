package ma.cdgk.integration.camel.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.core.data.BytesCloudEventData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.cdgk.integration.camel.util.Utils;
import ma.cdgk.integration.common.QueueTopicPair;
import ma.cdgk.integration.common.SourceDestinationConfig;
import ma.cdgk.integration.normalizer.EventNormalizer;
import org.apache.camel.Exchange;
import org.apache.camel.component.activemq.ActiveMQQueueEndpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class JmsToKafkaProcessor implements org.apache.camel.Processor {

    public static final String APPLICATION_AVRO = "application/avro";
    private final SourceDestinationConfig sourceDestinationConfig;
    private final ApplicationContext applicationContext;
    private QueueTopicPair queueTopicPair;

    @Value("${camel.component.kafka.schema-registry-u-r-l}")
    private String schemaRegistryUrl;

    QueueTopicPair getQueueTopicPairFromQueName(String queueName) {
        return sourceDestinationConfig.getJmsToKafkaQueueTopicPairs().stream()
                .filter(qTPair -> queueName.equals(qTPair.getQueue()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        ActiveMQQueueEndpoint queueEndpoint = (ActiveMQQueueEndpoint) exchange.getFromEndpoint();
        queueTopicPair = getQueueTopicPairFromQueName(queueEndpoint.getDestinationName());
        Class<?> clazz = Class.forName(queueTopicPair.getQueueMappingClass());
        Object body = new ObjectMapper().convertValue(exchange.getIn().getBody(), clazz);
        if (Utils.TopicFormat.CLOUD_EVENT.getFormatName().equals(queueTopicPair.getTopicFormat())) {
            EventNormalizer<Object, Object> normalizer = getNormaliser();
            Object object = normalizer.normalize(body);
            BytesCloudEventData bytesCloudEventData =
                    BytesCloudEventData.wrap(
                            Utils.serializeCloudEventData(
                                    object, schemaRegistryUrl, queueTopicPair.getTopic()));
            exchange.getMessage().setHeader("content-type", APPLICATION_AVRO);
            CloudEvent event =
                    CloudEventBuilder.v1()
                            .withId(UUID.randomUUID().toString()) // todo : what value should
                            // be
                            // injected : RANDOM ID
                            .withType(object.getClass().getName())
                            .withSource(URI.create("http://localhost")) // todo : what
                            // value should
                            // be injected
                            .withTime(OffsetDateTime.now())
                            .withDataContentType(APPLICATION_AVRO)
                            .withData(bytesCloudEventData)
                            .build();
            exchange.getIn().setBody(event);
        }
    }

    private EventNormalizer<Object, Object> getNormaliser() throws ClassNotFoundException {
        return (EventNormalizer<Object, Object>)
                applicationContext.getBean(Class.forName(queueTopicPair.getNormalizer()));
    }
}
