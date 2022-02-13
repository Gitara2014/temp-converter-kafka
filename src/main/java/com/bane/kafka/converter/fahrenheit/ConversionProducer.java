package com.bane.kafka.converter.fahrenheit;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.util.Properties;

// Publishes result to 'conversions_successful' topic, with the uuid and the fahrenheit_temperature in the message, or
// (in case of failure) sends the message to 'conversions_failed' topic with the uuid and the reason message.
@Component
public class ConversionProducer {

    private final KafkaProducer<String, String> producer;

    public ConversionProducer() {
        Properties settings = new Properties();
        settings.put("client.id", "conversion-producer");
        settings.put("bootstrap.servers", "localhost:9092");
        settings.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        settings.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(settings);
    }

    public void sendConversionResult(String topic, String uuid, String fahrenheitTemp) {
        final ProducerRecord<String, String> record = new ProducerRecord<>(topic, uuid, fahrenheitTemp);
        producer.send(record);
    }

    public void close() {
        //producer.flush(); ?
        producer.close();
    }

    public void shutdown(KafkaProducer<String, String> producer) {
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    System.out.println("### Stopping Basic producer ###");
                    producer.close();
                }));
    }
}
