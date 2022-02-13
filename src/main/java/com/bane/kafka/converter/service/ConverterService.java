package com.bane.kafka.converter.service;

import com.bane.kafka.converter.controller.TempDTO;
import com.bane.kafka.converter.entity.Temperature;
import com.bane.kafka.converter.event.RequestConversionProducer;
import com.bane.kafka.converter.repository.TemperatureRepository;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Log4j2
@Getter
@Service
public class ConverterService {

    private final String topic = "covert_celsius_to_fahrenheit";
    private final TemperatureRepository temperatureRepository;
    private final RequestConversionProducer requestConversionProducer;

    public ConverterService(TemperatureRepository temperatureRepository,
                            RequestConversionProducer requestConversionProducer) {
        this.temperatureRepository = temperatureRepository;
        this.requestConversionProducer = requestConversionProducer;
    }

    private UUID generateUuid() {
        return UUID.randomUUID();
    }

    //TODO transactions
    public Temperature createTempEvent(TempDTO tempDTO) {
        Temperature t = new Temperature(generateUuid(),
                ZonedDateTime.now(),
                tempDTO.getTemperature(),
                EventStatus.PENDING);

        return temperatureRepository.save(t);
    }

    public void issueCreateTempEvent(Temperature tempEvent) {
        log.info("Issuing new temperature convert event");
        requestConversionProducer.send(
                topic,
                tempEvent.getUuid().toString(),
                String.valueOf(tempEvent.getTemperature()));

        //producer.close(); ? When to close/graceful shutdown?
    }

}