package com.bane.kafka.converter.fahrenheit;

import com.bane.kafka.converter.entity.Temperature;
import com.bane.kafka.converter.service.ConverterService;
import com.bane.kafka.converter.service.EventStatus;
import com.bane.kafka.converter.util.Topics;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Log4j2
@Component
public class FahrenheitConsumer {

    private final KafkaConsumer<String, String> consumer;
    private final ConverterService converterService;

    public static final String FAHRENHEIT_CONSUMER_GROUP = "fahrenheit_consumer_group";
    private final long pollInterval = 10 * 1000;

    public FahrenheitConsumer(ConverterService converterService) {
        this.converterService = converterService;
        log.info("FahrenheitConsumer: consumerWorks on every: " + pollInterval);
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, FAHRENHEIT_CONSUMER_GROUP);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "10000");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<>(properties);
        //TODO subs to Topics.CONVERSIONS_FAILED_TOPIC
        Collection<String> topics = List.of(Topics.CONVERSIONS_SUCCESSFUL_TOPIC);
        consumer.subscribe(topics);
    }


    @Scheduled(fixedRate = pollInterval)
    private void consumerWorks() {
        log.info("FahrenheitConsumer: consumerWorks on every: " + pollInterval);

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(pollInterval));
        if (records.isEmpty()) {
            System.out.println("****\n FahrenheitConsumer: NOTHING TO POLL****");
        }
        for (ConsumerRecord<String, String> record : records) {

            System.out.println("******\n FahrenheitConsumer: RECORDS PRESENT. NOW POLLING:  ******\n");
            // offset = 8, key = f067b357-5bf0-4e59-8650-08ef3b7979f7, value = 37.2
            System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());

            //PERSIST RESULT
            saveResult(record.key(), record.value());
        }
    }

    private void saveResult(String uuid, String tempFahrenheit) {
        System.out.println("\n UUID: " + uuid);
        List<Temperature> temperatureList = converterService.getTemperatureRepository().findByUuid(UUID.fromString(uuid));
        Temperature temperature = temperatureList.get(0);
        System.out.println("\nFound temp: " + temperature);
        temperature.setTempFahrenheit(Double.valueOf(tempFahrenheit));
        temperature.setStatus(EventStatus.SUCCESS);
        Temperature savedTemp = converterService.getTemperatureRepository().save(temperature);
        System.out.println(savedTemp);
    }

}
