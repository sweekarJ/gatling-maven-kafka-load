package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.util.Properties
import scala.concurrent.duration._

class KafkaLoadSimulation extends Simulation {

  val topicName = "test-topic"

  val kafkaProperties: Properties = new Properties()
  kafkaProperties.load(getClass.getResourceAsStream("/kafka-config.properties"))

  val producer = new KafkaProducer[String, String](kafkaProperties)

  val scn: ScenarioBuilder = scenario("Kafka Load Test")
    .exec { session =>
      val message = """{"key": "value"}""" // Replace with your JSON data
      val record = new ProducerRecord[String, String](topicName, message)
      producer.send(record)
      session
    }

  setUp(
    scn.inject(
      constantUsersPerSec(500) during (60.seconds) // Adjust users and duration
    ).protocols()
  )

  after {
    producer.close()
  }
}
