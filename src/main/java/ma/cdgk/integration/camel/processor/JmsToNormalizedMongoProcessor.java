package ma.cdgk.integration.camel.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.core.v1.CloudEventV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.cdgk.integration.camel.mongoevent.MongoEvent;
import ma.cdgk.integration.camel.util.Utils;
import ma.cdgk.integration.common.QueueTopicPair;
import ma.cdgk.integration.common.SourceDestinationConfig;
import org.apache.camel.Exchange;
import org.apache.camel.component.activemq.ActiveMQQueueEndpoint;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class JmsToNormalizedMongoProcessor implements org.apache.camel.Processor {

    private final SourceDestinationConfig sourceDestinationConfig;

    @Value("${camel.component.kafka.schema-registry-u-r-l}")
    private String schemaRegistryUrl;

    @Override
    public void process(Exchange exchange) throws Exception {
        ActiveMQQueueEndpoint queueEndpoint = (ActiveMQQueueEndpoint) exchange.getFromEndpoint();
        QueueTopicPair queueTopicPair =
                Utils.getQueueTopicPairFromConfig(
                        sourceDestinationConfig.getJmsToKafkaQueueTopicPairs(),
                        qtp -> queueEndpoint.getDestinationName().equals(qtp.getQueue()));
        CloudEventV1 payload = exchange.getIn().getBody(CloudEventV1.class);
        Object event =
                Utils.deserializeCloudEventData(
                        queueTopicPair.getTopic(), payload.getData(), schemaRegistryUrl);
        Map<String, Object> body =
                new ObjectMapper()
                        .readValue(
                                event.toString(), new TypeReference<HashMap<String, Object>>() {});

        Map<String, Object> mongoBody = getMongoBody(payload, body);
        exchange.getIn().setBody(mongoBody);
    }

    private Map<String, Object> getMongoBody(CloudEventV1 payload, Map<String, Object> body) {
        MongoEvent mongoEvent =
                MongoEvent.builder()
                        .id(new ObjectId(new Date()))
                        .aggregateId(UUID.randomUUID().toString())
                        .captureDate(LocalDateTime.now())
                        .payload(body)
                        .eventType(payload.getType())
                        .build();
        return new ObjectMapper().convertValue(mongoEvent, Map.class);
    }
}
