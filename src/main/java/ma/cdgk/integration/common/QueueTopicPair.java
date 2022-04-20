package ma.cdgk.integration.common;

import lombok.Data;

@Data
public class QueueTopicPair {
    private String queue;
    private String topic;
    private String topicFormat;
    private String queueFormat;
    private String topicMappingClass;
    private String queueMappingClass;
    private String sourceSysteme;
    private String normalizer;
    private String jsonMapper;
    private String xmlMapper;
    private String destinationSource;
    private String mongoTechnicalJournaly;
    private String mongoNormalizedJournaly;
}
