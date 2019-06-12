package events;

import actions.ActionConfiguration;
import model.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class EventAction extends Model {

    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private ConsumerConfiguration consumerConfiguration;

    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private ActionConfiguration actionConfiguration;

    public EventAction(ConsumerConfiguration consumerConfiguration, ActionConfiguration actionConfiguration) {
        this.consumerConfiguration = consumerConfiguration;
        this.actionConfiguration = actionConfiguration;
    }

    public EventAction() {
    }

    public ConsumerConfiguration getConsumerConfiguration() {
        return this.consumerConfiguration;
    }

    public ActionConfiguration getActionConfiguration() {
        return this.actionConfiguration;
    }
}
