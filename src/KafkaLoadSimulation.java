package simulations;

import io.gatling.core.Predef.*;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.core.Simulation;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.core.OpenInjectionStep.atOnceUsers;

public class KafkaLoadSimulation extends Simulation {

    // Kafka Configuration
    private static KafkaProducer<String, String> createKafkaProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092"); // Update with your Kafka broker address
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer<>(props);
    }

    private static void sendMessages(String topic, int count) {
        KafkaProducer<String, String> producer = createKafkaProducer();
        for (int i = 0; i < count; i++) {
            producer.send(new ProducerRecord<>(topic, "key-" + i, "message-" + i));
        }
        producer.close();
    }

    // Simulation
    {
        int messageCount = 300; // Number of messages
        String topic = "test-topic"; // Kafka topic name

        setUp(
                scenario("Kafka Load Test")
                        .exec(session -> {
                            sendMessages(topic, messageCount);
                            return session;
                        })
                        .injectOpen(atOnceUsers(1))
        );
    }
}
