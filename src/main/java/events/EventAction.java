package events;

import actions.ActionConfiguration;

public class EventAction {

    private ConsumerConfiguration consumerConfiguration;
    private ActionConfiguration actionConfiguration;

    public EventAction(ConsumerConfiguration consumerConfiguration, ActionConfiguration actionConfiguration) {
        this.consumerConfiguration = consumerConfiguration;
        this.actionConfiguration = actionConfiguration;
    }

    public ConsumerConfiguration getConsumerConfiguration() {
        return this.consumerConfiguration;
    }

    public ActionConfiguration getActionConfiguration() {
        return this.actionConfiguration;
    }
}
