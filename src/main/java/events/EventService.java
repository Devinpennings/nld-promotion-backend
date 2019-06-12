package events;

import actions.ActionConfiguration;
import actions.ActionHandler;
import actions.mailing.MailTemplate;
import actions.mailing.MailingActionConfiguration;
import events.kafka.KafkaConsumerConfiguration;
import model.fluid.FluidKeyValueModel;
import model.fluid.FluidModel;
import org.reflections.Reflections;
import service.TriggerService;
import util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class EventService {

    @Inject
    private Logger logger;

    @Inject
    private TriggerService triggerService;

    private Set<EventConsumer> consumers;
    private Set<ActionHandler> handlers;

    @PostConstruct
    public void postConstruct() {
        this.consumers = new HashSet<>();
        this.handlers = new HashSet<>();
    }

    public void start() {

        this.logger.info("Starting events.EventService...");

        this.initConsumers(this.scanEventConsumers());
        this.initHandlers(this.scanActionHandlers());

        Collection<EventAction> eventActions = this.getEventActions();
        eventActions.forEach(this::setupEventActionHandler);

        this.triggerService.onNewEventAction(this::setupEventActionHandler);

        this.consumers.forEach(EventConsumer::start);

        this.logger.info("events.EventService started successfully.");

    }

    private void setupEventActionHandler(EventAction ea) {

        Optional<EventConsumer> consumer = this.findEventConsumer(ea.getConsumerConfiguration());
        consumer.ifPresent(eventConsumer -> eventConsumer.listen(ea.getConsumerConfiguration(), value -> {

            Collection<ActionConfiguration> actions = this.triggerService.getActionsForTrigger(ea.getConsumerConfiguration().getId());
            actions.forEach(a -> {

                if (a.getEnabled()) {
                    Optional<ActionHandler> handler = this.findActionHandler(a);
                    handler.ifPresent(actionHandler -> actionHandler.handle(a, value));
                }
            });
        }));
    }

    public void stop() {
        this.consumers.forEach(EventConsumer::stop);
    }

    private Collection<EventAction> getEventActions() {

        return this.triggerService.getEventActions();

//        Set<EventAction> set = new HashSet<>();
//        Set<String> requiredFields = new HashSet<>(Arrays.asList(
//                "id",
//                "email",
//                "status",
//                "amount"
//        ));
//        FluidKeyValueModel model = new FluidKeyValueModel(requiredFields);
//        set.add(
//            new EventAction(
//                new KafkaConsumerConfiguration("Betaling geslaagd", "Deze trigger wordt uitgevoerd wanneer een betaling geslaagd is.", "default", "paymentCreated", model),
//                new MailingActionConfiguration(model, "email", new MailTemplate("Betaling geslaagd", "Deze template wordt gebruikt wanneer een betaling geslaagd is", "Betaling geslaagd!",
//                        "<html>\n" +
//                        " <head>\n" +
//                        "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
//                        "  <title>Next Level Dining | Email</title>\n" +
//                        "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
//                        "</head>\n" +
//                        "<body>\n" +
//                        "Status: {{status}} " +
//                        "Betaald: {{amount}}\n" +
//                        "</body>\n" +
//                        "</html>", requiredFields))
//            )
//        );
//        return set;
    }

    public Set<Class<? extends EventConsumer>> scanEventConsumers() {

        Reflections reflections = new Reflections("events");
        return reflections.getSubTypesOf(EventConsumer.class);

    }

    public Set<Class<? extends ActionHandler>> scanActionHandlers() {

        Reflections reflections = new Reflections("actions");
        return reflections.getSubTypesOf(ActionHandler.class);

    }

    private Optional<EventConsumer> findEventConsumer(ConsumerConfiguration consumerConfiguration) {
        return this.consumers.stream()
                                .filter(c -> c.handlesConfiguration(consumerConfiguration))
                                .findFirst();
    }

    private Optional<ActionHandler> findActionHandler(ActionConfiguration actionConfiguration) {
        return this.handlers.stream()
                                .filter(h -> h.handlesAction(actionConfiguration))
                                .findFirst();
    }

    private void initConsumers(Set<Class<? extends EventConsumer>> types) {

        types.forEach(type -> this.consumers.add(CDI.current().select(type).get()));

    }

    private void initHandlers(Set<Class<? extends ActionHandler>> types) {
        types.forEach(type -> this.handlers.add(CDI.current().select(type).get()));
    }

    public Collection<EventConsumer> getEventConsumers() {
        return this.consumers;
    }
}
