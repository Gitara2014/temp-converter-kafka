package com.bane.kafka.converter;

import com.bane.kafka.converter.entity.Temperature;
import com.bane.kafka.converter.repository.TemperatureRepository;
import com.bane.kafka.converter.service.EventStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@SpringBootApplication
public class TemperatureConverterApplication {

    public static void main(String[] args) {

        SpringApplication.run(TemperatureConverterApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(TemperatureRepository repository) {
        return (args) -> {
            UUID uuid1 = UUID.randomUUID();
            UUID uuid2 = UUID.randomUUID();

            // save a few customers
            repository.save(new Temperature(uuid1, ZonedDateTime.now(), 36.4, null, EventStatus.PENDING));
            repository.save(new Temperature(uuid1, ZonedDateTime.now(), 36.4, 97.52, EventStatus.SUCCESS));

            repository.save(new Temperature(uuid2, ZonedDateTime.now(), 20.3, null, EventStatus.FAILURE));

            // fetch all Temps
            log.info("Temps found with findAll():");
            log.info("-------------------------------");
            for (Temperature temp : repository.findAll()) {
                log.info(temp.toString());
            }
            log.info("");

            // fetch an individual temp by ID
            Optional<Temperature> optionalTemperature = repository.findById(1L);
            if (optionalTemperature.isPresent()) {
                log.info("Temp found with findById(1L):");
                log.info("--------------------------------");
                log.info(optionalTemperature.get().toString());
                log.info("");

            }

            List<Temperature> tempList = repository.findByUuid(uuid1);
            Optional<Temperature> findByUuidOptional = tempList.stream().findAny();
            if (findByUuidOptional.isPresent()) {
                findByUuidOptional.get();
                log.info("Temp found with findByUuid(uuid):");
                log.info(findByUuidOptional.get().toString());
                log.info("--------------------------------");
            }
            ;


        };
    }

}
