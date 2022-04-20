package ma.cdgk.integration.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("source-destination-config")
@Component
public class SourceDestinationConfig {

    private List<QueueTopicPair> jmsToKafkaQueueTopicPairs = new ArrayList<>();
    private List<QueueTopicPair> kafkaToJmsQueueTopicPairs = new ArrayList<>();

    public List<QueueTopicPair> getKafkaToJmsQueueTopicPairs() {
        return kafkaToJmsQueueTopicPairs;
    }

    public void setKafkaToJmsQueueTopicPairs(List<QueueTopicPair> kafkaToJmsQueueTopicPairs) {
        this.kafkaToJmsQueueTopicPairs = kafkaToJmsQueueTopicPairs;
    }

    public List<QueueTopicPair> getJmsToKafkaQueueTopicPairs() {
        return jmsToKafkaQueueTopicPairs;
    }

    public void setJmsToKafkaQueueTopicPairs(List<QueueTopicPair> jmsToKafkaQueueTopicPairs) {
        this.jmsToKafkaQueueTopicPairs = jmsToKafkaQueueTopicPairs;
    }
}
