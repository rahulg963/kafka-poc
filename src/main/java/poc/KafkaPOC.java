package poc;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Properties;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAsync
public class KafkaPOC {
    public static void main(String[] args) {
        SpringApplication.run(KafkaPOC.class, args);

//      setting kafka without spring kafka utility
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092,localhost:9093");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer kafkaProducer = new KafkaProducer(properties);
        ProducerRecord producerRecord = new ProducerRecord("my_topic", "partition_key", "message_payload");
        try{
            kafkaProducer.send(producerRecord);
        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }finally {
            kafkaProducer.close();
        }

    }
}
