import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Properties;

import org.json.*;


public class Consumer {
    public static void main(String[] args) {
        Consumer kafkaConsumer = new Consumer();
        kafkaConsumer.readMsg();
    }

    private void readMsg() {
        String bootstrapServers = "127.0.0.1:9092";
        String groupId = "test-application";
        String topic = "sync.mytestdb.courses";

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        final org.apache.kafka.clients.consumer.Consumer<String, String> consumer =
                new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(topic));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                if (record.value() == null) {
                    continue;
                }
                JSONObject obj = new JSONObject(record.value());
                JSONObject payload = obj.getJSONObject("payload");

                Object before = payload.get("before");
                System.out.println("value before " + before);

                Object after = payload.get("after");
                System.out.println("value after " + after);

                System.out.println("----------------------");
            }
        }
    }
}
