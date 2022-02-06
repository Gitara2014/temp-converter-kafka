package com.bane.kafka.converter.service;

import com.bane.kafka.converter.TempDTO;
import com.bane.kafka.converter.entity.Temperature;
import com.bane.kafka.converter.repository.TemperatureRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Service
public class ConverterService {

    private final TemperatureRepository temperatureRepository;

    public ConverterService(TemperatureRepository temperatureRepository) {
        this.temperatureRepository = temperatureRepository;
    }

    private UUID generateUuid() {
        return UUID.randomUUID();
    }

    public Temperature createTempEvent(TempDTO tempDTO) {
        Temperature t = new Temperature(generateUuid(),
                ZonedDateTime.now(),
                tempDTO.getTemperature(),
                EventStatus.PENDING);

        return temperatureRepository.save(t);
    }
}