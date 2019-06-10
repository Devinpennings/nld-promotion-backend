package events.kafka;

import events.ConsumerConfiguration;
import model.fluid.FluidModel;

public class KafkaConsumerConfiguration implements ConsumerConfiguration {

    private String topicName;
    private String eventName;
    private FluidModel response;

    public KafkaConsumerConfiguration(String topicName, String eventName, FluidModel expectedResponse) {
        this.topicName = topicName;
        this.eventName = eventName;
        this.response = expectedResponse;
    }

    public String getTopicName() {
        return this.topicName;
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
