package ma.cdgk.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "event")
@JsonRootName(value = "event")
@XmlAccessorType(XmlAccessType.FIELD)
public class Event {

    @XmlElement(name = "event_type")
    @JsonProperty("event_type")
    private String eventType;
}
