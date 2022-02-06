package com.bane.kafka.converter.service;

import com.bane.kafka.converter.TempDTO;
import com.bane.kafka.converter.entity.Temperature;
import com.bane.kafka.converter.repository.TemperatureRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class ConverterServiceTest {

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Test
    void generateUniqueUUID() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        assertThat(uuid1).isNotEqualTo(uuid2);
    }

    @Disabled
    @Test
    void generateTemperatureService() {
        //arrange
        Double temp = 37.5;
        String device = "IOT-WINE-FIELD-SENSOR-ABC123567";

        TempDTO tempDTO = new TempDTO(temp, device);
        //act
        ConverterService converterService = new ConverterService(temperatureRepository);
        //assert
        Temperature tempEvent = converterService.createTempEvent(tempDTO);
        assertThat(tempEvent.getTemperature()).isEqualTo(temp);
        assertThat(tempEvent.getDate()).isInstanceOf(ZonedDateTime.class).isNotNull();
        assertThat(tempEvent.getStatus()).isEqualTo(EventStatus.PENDING);
        assertThat(tempEvent.getUuid()).isInstanceOf(UUID.class).isNotNull();
    }
}