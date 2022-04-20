package ma.cdgk.integration.camel.mongoevent;

import lombok.*;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MongoEvent {

    private ObjectId id;

    private String aggregateId;

    private String sourceSystemCode;

    private String sourceTableName;

    private String sourceRowId;

    private LocalDateTime sourceTimestamp;

    private LocalDate sourceDate;

    private LocalDateTime timestamp;

    private LocalDateTime captureDate;

    private String dmlType;

    private String eventType;

    private Object payload;

    private String version;
}
