//package poc.kafkaConfig;
//
////import org.apache.kafka.clients.admin.AdminClientConfig;
////import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.KafkaAdmin;
//
//import javax.validation.Valid;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author Rahul Goel on 04/03/19
// */
//
//@Configuration
//public class KafkaTopicConfig {
////
////    @Value(value = "${kafka.bootstrapAddress}")
////    private String bootstrapAddress;
////
////    @Value(value = "${kafka.register.topic}")
////    private String newTopic;
////
////    @Bean
////    public KafkaAdmin kafkaAdmin() {
////        Map<String, Object> configs = new HashMap<>();
////        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
////        return new KafkaAdmin(configs);
////    }
////
//////    This will create new topic with default partions and replica assignment
////    @Bean
////    public NewTopic topic1() {
////        return new NewTopic(newTopic, 1, (short) 1);
////    }
//}