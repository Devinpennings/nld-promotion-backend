package events.kafka;

import events.EventConfiguration;
import model.fluid.FluidModel;

public class KafkaEventConfiguration implements EventConfiguration {

    private String topicName;
    private String eventName;
    private FluidModel response;

    public KafkaEventConfiguration(String topicName, String eventName, FluidModel expectedResponse) {
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
