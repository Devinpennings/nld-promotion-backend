package service;

import actions.ActionConfiguration;
import data.jpa.EventActionJPADAO;
import data.jpa.KafkaConsumerConfigurationJPADAO;
import data.jpa.MailingActionConfigurationJPADAO;
import events.ConsumerConfiguration;
import events.EventAction;
import events.EventConsumer;
import events.EventService;
import events.kafka.KafkaConsumerConfiguration;
import util.Callback;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class TriggerService {

    @Inject
    private KafkaConsumerConfigurationJPADAO kafkaDAO;

    @Inject
    private EventActionJPADAO eventActionJPADAO;

    @Inject
    private MailingActionConfigurationJPADAO mailingDAO;

    @Inject
    private EventService eventService;

    private Set<Callback<EventAction>> eventActionCallbacks;

    @PostConstruct
    public void postConstruct() {
        this.eventActionCallbacks = new HashSet<>();
    }

    public Collection<? extends ConsumerConfiguration> get() {
        return this.kafkaDAO.get();
    }

    public Optional<? extends ConsumerConfiguration> get(String id) {
        return this.kafkaDAO.get(id);
    }

    public ConsumerConfiguration add(ConsumerConfiguration consumerConfiguration) {
        return this.kafkaDAO.add((KafkaConsumerConfiguration) consumerConfiguration);
    }

    public Optional<ActionConfiguration> add(ActionConfiguration actionConfiguration, String consumerId) {

        Optional<? extends ConsumerConfiguration> consumer = this.kafkaDAO.get(consumerId);
        if (consumer.isPresent()) {

            EventAction eventAction = new EventAction(consumer.get(), actionConfiguration);
            ActionConfiguration configuration = this.eventActionJPADAO.add(eventAction).getActionConfiguration();
            this.dispatchNewEventAction(eventAction);
            return Optional.of(configuration);

        }

        return Optional.empty();

    }

    public Collection<EventConsumer> getEventConsumers() {

        return this.eventService.getEventConsumers();

    }

    public Collection<ActionConfiguration> getActionsForTrigger(String id) {

        Collection<ActionConfiguration> result = new HashSet<>();

        Optional<? extends ConsumerConfiguration> consumerConfiguration = this.get(id);
        if (consumerConfiguration.isPresent()) {

            Collection<EventAction> eventActions = this.eventActionJPADAO.get().stream().filter(ea -> ea.getConsumerConfiguration().equals(consumerConfiguration.get())).collect(Collectors.toList());
            eventActions.forEach(ea -> result.add(ea.getActionConfiguration()));

        }

        return result;

    }

    public Collection<EventAction> getEventActions() {
        return this.eventActionJPADAO.get();
    }

    private void dispatchNewEventAction(EventAction eventAction) {
        this.eventActionCallbacks.forEach(c -> c.call(eventAction));
    }

    public void onNewEventAction(Callback<EventAction> setupEventActionHandler) {
        this.eventActionCallbacks.add(setupEventActionHandler);
    }
}
