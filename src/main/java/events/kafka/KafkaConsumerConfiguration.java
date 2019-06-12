package events.kafka;

import events.ConsumerConfiguration;
import model.Model;
import model.fluid.FluidKeyValueModel;
import model.fluid.FluidModel;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class KafkaConsumerConfiguration extends ConsumerConfiguration {

    private String title;
    private String description;
    private String topicName;
    private String eventName;

    @OneToOne(cascade = {CascadeType.ALL})
    private FluidKeyValueModel response;

    public KafkaConsumerConfiguration() {
    }

    public KafkaConsumerConfiguration(String title, String description, String topicName, String eventName, FluidModel expectedResponse) {
        this.title = title;
        this.description = description;
        this.topicName = topicName;
        this.eventName = eventName;
        this.response = (FluidKeyValueModel) expectedResponse;
    }

    public String getTopicName() {
        return this.topicName;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getEventName() {
        return this.eventName;
    }

    @Override
    public FluidModel getExpectedResponse() {
        return this.response;
    }

}
