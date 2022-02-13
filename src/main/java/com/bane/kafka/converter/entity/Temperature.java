package com.bane.kafka.converter.entity;

import com.bane.kafka.converter.service.EventStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@ToString
@Setter
@Getter
@Entity
public class Temperature {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "uuid", nullable = false)
    private UUID uuid;
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;
    @Column(name = "temperature_celsius", nullable = false)
    private Double tempCelsius;
    @Column(name = "temperature_fahrenheit")
    private Double tempFahrenheit;
    @Column(name = "status", nullable = false)
    private EventStatus status;

    protected Temperature() {
    }

    public Temperature(UUID uuid, ZonedDateTime date, Double tempCelsius, Double tempFahrenheit, EventStatus status) {
        this.uuid = uuid;
        this.date = date;
        this.tempCelsius = tempCelsius;
        this.tempFahrenheit = tempFahrenheit;
        this.status = status;
    }
}
