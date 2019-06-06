package events.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import events.Event;
import events.EventConfiguration;
import events.EventConsumer;
import model.fluid.FluidKeyValueModel;
import model.fluid.FluidModel;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import util.Callback;
import util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.NotSupportedException;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class KafkaConsumer implements EventConsumer {

    @Inject
    private Logger logger;

    private org.apache.kafka.clients.consumer.KafkaConsumer<String, String> kafkaConsumer;
    private Map<KafkaEventConfiguration, Set<Callback<FluidModel>>> callbacks;
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
        List<String> topics = this.getEventsAsStringList();
        this.kafkaConsumer.subscribe(topics);

        new Thread(() -> {
            while (this.running) {
                ConsumerRecords<String, String> records = this.kafkaConsumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    // Get the callback collections
                    // Filter by topic
                    // Filter by event tame
                    // For all callbacks call with the event
                    for (KafkaEventConfiguration ks : this.callbacks.keySet()) {
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

    public void listen(EventConfiguration event, Callback callback) {

        if (event.getClass() != KafkaEventConfiguration.class) {
            throw new NotSupportedException("Configuration type not supported");
        }

        if (this.callbacks.containsKey(event)) {
            this.callbacks.get(event).add(callback);
        } else {
            this.callbacks.put((KafkaEventConfiguration) event, new HashSet<>());
            this.callbacks.get(event).add(callback);
        }

    }

    public void ignore(EventConfiguration event) {

    }

    @Override
    public boolean handlesConfiguration(EventConfiguration configuration) {
        return configuration.getClass() == KafkaEventConfiguration.class;
    }

    private List<String> getEventsAsStringList() {
        return this.callbacks.keySet().stream()
                                .map(KafkaEventConfiguration::getTopicName)
                                .collect(Collectors.toList());
    }

    private void initKafkaConsumer() {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "mail-service");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        this.kafkaConsumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(props);

    }

}
