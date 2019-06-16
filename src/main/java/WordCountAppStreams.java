import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.Properties;

/**
 * @author Rahul Goel on 16/06/19
 */

public class WordCountAppStreams {
    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.103:9092");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> textLines = streamsBuilder.stream("kafka-streams-sample-input");

        KTable<String, Long> wordCounts = textLines
                // 2 - map values to lowercase
                .mapValues(textLine -> textLine.toLowerCase())
                // can be alternatively written as:
                // .mapValues(String::toLowerCase)
                // 3 - flatmap values split by space
                .flatMapValues(
                        textLine -> Arrays.asList(textLine.split("\\W+")))
                // 4 - select key to apply a key (we discard the old key)
                .selectKey((key, word) -> word)
                // 5 - group by key before aggregation
                .peek((key, value) -> {
                    System.out.print("=================");
                    System.out.print(key);
                    System.out.print("=================");
                })
                .groupByKey()
                // 6 - count occurrences
                .count()
                ;

        // 7 - to in order to write the results back to kafka
        wordCounts.toStream().to("kafka-streams-sample-output", Produced.with(Serdes.String(), Serdes.Long()));

        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), config);
        kafkaStreams.start();

//        // shutdown hook to correctly close the streams application
//        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
//
//        // Update:
//        // print the topology every 10 seconds for learning purposes
//        while(true){
//            System.out.println(kafkaStreams.toString());
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                break;
//            }
//        }
    }
}
