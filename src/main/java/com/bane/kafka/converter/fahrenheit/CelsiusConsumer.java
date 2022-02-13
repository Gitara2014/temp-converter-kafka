package com.bane.kafka.converter.fahrenheit;

import com.bane.kafka.converter.exception.TemperatureConversionException;
import com.bane.kafka.converter.util.Topics;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Properties;


@Log4j2
@Component
public class CelsiusConsumer {

    private final KafkaConsumer<String, String> consumer;
    private final long pollInterval = 10 * 1000;
    private final ConversionProducer conversionProducer;

    public CelsiusConsumer(ConversionProducer conversionProducer) {
        this.conversionProducer = conversionProducer;
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "celsius_consumer_group");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "10000");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<>(properties);
        Collection<String> topics = List.of(Topics.COVERT_CELSIUS_TO_FAHRENHEIT);
        consumer.subscribe(topics);
    }

    @Scheduled(fixedRate = pollInterval)
    private void consumerWorks() {
        log.info("CelsiusConsumer: consumerWorks on every: " + pollInterval);

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(pollInterval));
        if (records.isEmpty()) {
            System.out.println("****CelsiusConsumer : NOTHING TO POLL****");
        }
        for (ConsumerRecord<String, String> record : records) {

            System.out.println("******CelsiusConsumer:  RECORDS PRESENT. NOW POLLING:  ******\n");
            // offset = 8, key = f067b357-5bf0-4e59-8650-08ef3b7979f7, value = 37.2
            System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());

            //CONVERT

            try {

//                    if(1 == 1){
//                        throw new TemperatureConversionException("enforce fail convert");
//                    }

                double fahrenheit = CelsiusToFahrenheit.fahrenheit(record.value());
                //PUBLISH SUCCESS CONVERSION
                conversionProducer.sendConversionResult(Topics.CONVERSIONS_SUCCESSFUL_TOPIC, record.key(), String.valueOf(fahrenheit));

            } catch (TemperatureConversionException e) {
                log.error("Failed to convert: " + record.value());
                //PUBLISH FAILED CONVERSION
                conversionProducer.sendConversionResult(Topics.CONVERSIONS_FAILED_TOPIC, record.key(), record.value());
            }
        }
    }

}
