package com.bane.kafka.converter.controller;

import com.bane.kafka.converter.entity.Temperature;
import com.bane.kafka.converter.service.ConverterService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Log4j2
@AllArgsConstructor
@RestController
public class ConvertController {

    private final ConverterService converterService;


    // curl -X POST -H "Content-Type: application/json" -d '{"temperature": "39.2"}' 'http://www.localhost:8585/temperature'
    // curl -X POST -H "Content-Type: application/json" -d '{"temperature": 37.2, "device": "IOT-WINE-FIELD-SENSOR-ABC123567"}' 'http://www.localhost:8585/temperature'
    @PostMapping("/temperature")
    public Temperature createTemperature(@RequestBody TempDTO temperature) {
        Temperature tempEvent = converterService.createTempEvent(temperature);
        converterService.issueCreateTempEvent(tempEvent);
        log.info(tempEvent);
        return tempEvent;
    }

    // curl http://www.localhost:8585/temperature?uuid=0044a43a-ec2c-4549-bf03-bdc78ca6faa7
    @GetMapping("/temperature")
    public Temperature convert(@RequestParam("uuid") String uuid) throws Throwable {
        List<Temperature> temperatureList = converterService.getTemperatureRepository()
                .findByUuid(UUID.fromString(uuid));
        return temperatureList.stream()
                .findFirst()
                .orElseThrow(new Supplier<Throwable>() {
                    @Override
                    public Throwable get() {
                        return new IllegalArgumentException("not found");
                    }
                });
    }
}
