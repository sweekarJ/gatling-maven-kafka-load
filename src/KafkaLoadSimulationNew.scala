package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.util.Properties

class KafkaLoadSimulationNew extends Simulation {

  // Kafka Configuration
  def createKafkaProducer(): KafkaProducer[String, String] = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092") // Update with your Kafka broker
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    new KafkaProducer[String, String](props)
  }

  // Sending messages to Kafka
  def sendMessages(topic: String, count: Int): Unit = {
    val producer = createKafkaProducer()
    for (i <- 1 to count) {
      producer.send(new ProducerRecord[String, String](topic, s"key-$i", s"message-$i"))
    }
    producer.close()
  }

  // Define the Scenario
  val kafkaScenario: ScenarioBuilder = scenario("Kafka Load Test")
    .exec(session => {
      sendMessages("test-topic", 50)  // Send 5000 messages to "test-topic"
      session
    })

  // Set up the simulation
  setUp(
    kafkaScenario.inject(atOnceUsers(1)) // One user at a time to simulate load
  )
}
