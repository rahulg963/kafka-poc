package poc;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Future;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAsync
public class KafkaPOC {
    public static void main(String[] args) {

//        check this out : https://www.baeldung.com/spring-kafka
        SpringApplication.run(KafkaPOC.class, args);

//      setting kafka without spring kafka utility
        String topic = "my_topic";
        Properties properties = new Properties();
//        common properties
        properties.put("bootstrap.servers", "localhost:9092,localhost:9093");

//        producer properties
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

//        consumer properties
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "test");

//        Kafka Producer
        KafkaProducer kafkaProducer = new KafkaProducer(properties);
        ProducerRecord producerRecord = new ProducerRecord(topic, "partition_key", "message_payload");
        try{
            Future<RecordMetadata> future = kafkaProducer.send(producerRecord);
        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }finally {
            kafkaProducer.close();
        }

//        kafka Consumer
        KafkaConsumer kafkaConsumer = new KafkaConsumer(properties);
        kafkaConsumer.subscribe(Arrays.asList(topic));
//        for specfic partition, not recommended
//        kafkaConsumer.assign();
        try {
            while (true){
                Iterable<ConsumerRecord> recordList = kafkaConsumer.poll(10).records(topic);
                recordList.forEach(record -> System.out.println("Message Received : " + record.toString()));
//                deafult auto commit on time expiry
//            blocking
//                kafkaConsumer.commitSync();

//            non-blocking
//                kafkaConsumer.commitAsync(new OffsetCommitCallback() {
//                    @Override
//                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
//
//                    }
//                });
            }
        }
        finally {
            kafkaConsumer.close();
        }
    }
}
