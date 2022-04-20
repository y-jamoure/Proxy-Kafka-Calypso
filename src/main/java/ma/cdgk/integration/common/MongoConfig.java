package ma.cdgk.integration.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("mongo")
@Component
@Getter
@Setter
public class MongoConfig {

    private String url;
    private String database;
    private String normalizedCollection;
    private String technicalCollection;
}
