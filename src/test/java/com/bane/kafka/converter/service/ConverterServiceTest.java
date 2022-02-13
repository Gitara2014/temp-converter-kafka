package com.bane.kafka.converter.service;

import com.bane.kafka.converter.repository.TemperatureRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

}