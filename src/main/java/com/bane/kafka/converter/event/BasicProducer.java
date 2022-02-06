package com.bane.kafka.converter.event;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class BasicProducer {

    public static void main(String[] args) {
        Properties settings = new Properties();
        settings.put("client.id", "basic-producer-v0.1.0");
        settings.put("bootstrap.servers", "localhost:9092");
        settings.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        settings.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        final KafkaProducer<String, String> producer = new KafkaProducer<String, String>(settings);

        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    System.out.println("### Stopping Basic producer ###");
                    producer.close();
                }));

        final String topic = "hello_world_topic";
        for (int i = 1; i < 5; i++) {
            final String key = "key-" + i;
            final String value = "value-" + i;
            final ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);

            producer.send(record);
        }

    }
}
