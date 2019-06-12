package events.kafka;

import events.ConsumerConfiguration;
import events.EventConsumer;
import model.fluid.FluidModel;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import util.Callback;
import util.logging.Logger;
import util.properties.Property;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.NotSupportedException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class KafkaConsumer implements EventConsumer {

    private static final String id = "47e05d7a-d44b-4c15-8a0f-1feeb5062358";
    private static final String title = "Kafka";
    private static final String description = "Kafka queue verbruiker.";

    @Inject
    private Logger logger;

    @Inject
    @Property("kafka.host")
    private String host;

    @Inject
    @Property("kafka.group")
    private String group;

    @Inject
    @Property("kafka.username")
    private String username;

    @Inject
    @Property("kafka.password")
    private String password;

    @Inject
    @Property("kafka.prefix")
    private String prefix;

    private org.apache.kafka.clients.consumer.KafkaConsumer<String, String> kafkaConsumer;
    private Map<KafkaConsumerConfiguration, Set<Callback<FluidModel>>> callbacks;
    private boolean running;

    @PostConstruct
    public void postConstruct() {
        this.callbacks = new HashMap<>();
    }

    public void start() {

        this.logger.info("Starting kafka consumer...");

        if (this.kafkaConsumer == null) {
            initKafkaConsumer();
        }

        this.running = true;
        Set<String> topics = this.getEventsAsStringList();
        topics.add(this.prefix + "default");
        this.kafkaConsumer.subscribe(topics);

        new Thread(() -> {
            while (this.running) {
                ConsumerRecords<String, String> records = this.kafkaConsumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    // Get the callback collections
                    // Filter by topic
                    // Filter by event tame
                    // For all callbacks call with the event
                    for (KafkaConsumerConfiguration ks : this.callbacks.keySet()) {
                        if (ks.getTopicName().equals(record.topic())) {
                            if (ks.getEventName().equals(record.key())) {
                                this.callbacks.get(ks).forEach(cb ->
                                {
                                    try {
                                        ks.getExpectedResponse().seed(record.value());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    cb.call(ks.getExpectedResponse());
                                });
                            }
                        }
                    }
                }
            }
        }).start();

    }

    public void stop() {

        this.running = false;
        if (this.kafkaConsumer != null) {
            this.kafkaConsumer.unsubscribe();
        }

    }

    public void listen(ConsumerConfiguration event, Callback callback) {

        if (event.getClass() != KafkaConsumerConfiguration.class) {
            throw new NotSupportedException("Configuration type not supported");
        }

        if (this.callbacks.containsKey(event)) {
            this.callbacks.get(event).add(callback);
        } else {
            this.callbacks.put((KafkaConsumerConfiguration) event, new HashSet<>());
            this.callbacks.get(event).add(callback);
        }

    }

    public void ignore(ConsumerConfiguration event) {

        if (event.getClass() != KafkaConsumerConfiguration.class) {
            throw new NotSupportedException("Configuration type not supported");
        }

        Set<Callback<FluidModel>> callbacks = this.callbacks.get(event);
        callbacks.clear();

    }

    @Override
    public Class<? extends ConsumerConfiguration> getConfigurationType() {
        return KafkaConsumerConfiguration.class;
    }

    @Override
    public boolean handlesConfiguration(ConsumerConfiguration configuration) {
        return configuration.getClass() == KafkaConsumerConfiguration.class;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    private Set<String> getEventsAsStringList() {
        return this.callbacks.keySet().stream()
                                .map(kafkaConsumerConfiguration -> this.prefix + kafkaConsumerConfiguration.getTopicName())
                                .collect(Collectors.toSet());
    }

    private void initKafkaConsumer() {

        Properties props = new Properties();
        props.put("bootstrap.servers", this.host);
        props.put("group.id", this.group);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        if (this.username != null && !this.username.equals("") && this.password != null && !this.password.equals("")) {

            String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
            String jaasCfg = String.format(jaasTemplate, this.username, this.password);

            props.put("security.protocol", "SASL_SSL");
            props.put("sasl.mechanism", "SCRAM-SHA-256");
            props.put("sasl.jaas.config", jaasCfg);

        }

        this.kafkaConsumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(props);

    }

}
