package com.bane.kafka.converter.event;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class RequestConversionProducer {

    private final KafkaProducer<String, String> producer;

    public RequestConversionProducer() {
        Properties settings = new Properties();
        settings.put("client.id", "request-conversion-producer");
        settings.put("bootstrap.servers", "localhost:9092");
        settings.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        settings.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(settings);
    }

    public void send(String topicName, String key, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, key, value);
        producer.send(record);
    }

    public void close() {
        //producer.flush(); ?
        producer.close();
    }

    // ?
    public void shutdown(KafkaProducer<String, String> producer) {
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    System.out.println("### Stopping Basic producer ###");
                    producer.close();
                }));
    }

}
