package simulations

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

import java.util.Properties

object SimulationUtils {

  // Create Kafka Producer
  def createKafkaProducer(bootstrapServers: String): KafkaProducer[String, String] = {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    new KafkaProducer[String, String](props)
  }

  // Send messages to Kafka
  def sendMessages(producer: KafkaProducer[String, String], topic: String, count: Int): Unit = {
    for (i <- 1 to count) {
      producer.send(new ProducerRecord[String, String](topic, s"key-$i", s"message-$i"))
    }
    producer.close()
  }
}
