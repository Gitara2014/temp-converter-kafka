package com.bane.kafka.converter.controller;

import com.bane.kafka.converter.TempDTO;
import com.bane.kafka.converter.entity.Temperature;
import com.bane.kafka.converter.service.ConverterService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@RestController
public class ConvertController {

    private final ConverterService converterService;


    // curl -X POST -H "Content-Type: application/json" -d '{"temperature": "39.2"}' 'http://www.localhost:8080/temperature'
    // curl -X POST -H "Content-Type: application/json" -d '{"temperature": 37.2, "device": "IOT-WINE-FIELD-SENSOR-ABC123567"}' 'http://www.localhost:8080/temperature'
    @PostMapping("/temperature")
    public Temperature createTemperature(@RequestBody TempDTO temperature) {
        return converterService.createTempEvent(temperature);
    }

    // curl http://www.localhost:8080/temperature?uuid=0044a43a-ec2c-4549-bf03-bdc78ca6faa7
    @GetMapping("/temperature")
    public Temperature convert(@RequestParam("uuid") String uuid) {
        List<Temperature> temperatureList = converterService.getTemperatureRepository()
                .findByUuid(UUID.fromString(uuid));
        return temperatureList.stream()
                .findFirst()
                .orElse(null);
    }
}
