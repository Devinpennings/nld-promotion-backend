package events;

import actions.ActionConfiguration;

public class EventAction {

    private EventConfiguration eventConfiguration;
    private ActionConfiguration actionConfiguration;

    public EventAction(EventConfiguration eventConfiguration, ActionConfiguration actionConfiguration) {
        this.eventConfiguration = eventConfiguration;
        this.actionConfiguration = actionConfiguration;
    }

    public EventConfiguration getEventConfiguration() {
        return this.eventConfiguration;
    }

    public ActionConfiguration getActionConfiguration() {
        return this.actionConfiguration;
    }
}
