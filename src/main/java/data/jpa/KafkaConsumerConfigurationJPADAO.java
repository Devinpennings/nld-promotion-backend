package data.jpa;

import data.interfaces.KafkaConsumerConfigurationDAO;
import events.kafka.KafkaConsumerConfiguration;

public class KafkaConsumerConfigurationJPADAO extends ModelJPADAO<KafkaConsumerConfiguration> implements KafkaConsumerConfigurationDAO {
}
