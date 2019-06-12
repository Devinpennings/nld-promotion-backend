package app;

import actions.mailing.MailTemplate;
import actions.mailing.MailingActionConfiguration;
import events.ConsumerConfiguration;
import events.EventService;
import events.kafka.KafkaConsumerConfiguration;
import model.fluid.FluidKeyValueModel;
import model.fluid.FluidModel;
import service.TriggerService;
import util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Startup
@Singleton
public class MailApplication {

    @Inject
    private Logger logger;

    @Inject
    private EventService service;

    @Inject
    private TriggerService triggerService;

    @PostConstruct
    public void postConstruct() {

        this.logger.info("Starting mail service...");
        this.service.start();

        Set<String> requiredFields = new HashSet<>(Arrays.asList(
                "deliveryTime",
                "email"
        ));
        FluidModel model = new FluidKeyValueModel(requiredFields);
        ConsumerConfiguration consumerConfiguration = this.triggerService.add(
                new KafkaConsumerConfiguration(
                        "Bestelling geplaatst",
                        "Deze trigger wordt uitgevoerd wanneer een bestelling geplaatst is.",
                        "default",
                        "deliveryConfirmed",
                        model));

        this.triggerService.add(new MailingActionConfiguration(
                model, "email",
                new MailTemplate("Bestelling geplaatst", "Deze template wordt gebruikt wanneer een bestelling geplaatst is", "Bestelling geslaagd!",
                "<html>\n" +
                        " <head>\n" +
                        "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                        "  <title>Next Level Dining | Email</title>\n" +
                        "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "Uw bestelling is geplaatst, geschatte leveringstijd is: {{deliveryTime}} " +
                        "</body>\n" +
                        "</html>", requiredFields)),
                consumerConfiguration.getId());

    }
}
