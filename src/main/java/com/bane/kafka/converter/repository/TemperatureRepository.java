package com.bane.kafka.converter.repository;

import com.bane.kafka.converter.entity.Temperature;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface TemperatureRepository extends CrudRepository<Temperature, Long> {

    List<Temperature> findByUuid(UUID uuid);
}
