package com.bane.kafka.converter.service;

import com.bane.kafka.converter.controller.TempDTO;
import com.bane.kafka.converter.entity.Temperature;
import com.bane.kafka.converter.event.RequestConversionProducer;
import com.bane.kafka.converter.repository.TemperatureRepository;
import com.bane.kafka.converter.util.Topics;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Log4j2
@Getter
@Service
public class ConverterService {

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
                null,
                EventStatus.PENDING);

        return temperatureRepository.save(t);
    }

    public void issueCreateTempEvent(Temperature tempEvent) {
        log.info("Issuing new temperature convert event");
        requestConversionProducer.send(
                Topics.COVERT_CELSIUS_TO_FAHRENHEIT,
                tempEvent.getUuid().toString(),
                String.valueOf(tempEvent.getTempCelsius()));

        //producer.close(); ? When to close/graceful shutdown?
    }

}