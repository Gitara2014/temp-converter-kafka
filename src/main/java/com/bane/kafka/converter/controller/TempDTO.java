package com.bane.kafka.converter.controller;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TempDTO {
    private Double temperature;
    private String device;

    @JsonCreator
    public TempDTO(@JsonProperty("temperature") Double temperature, @JsonProperty("device") String device) {
        this.temperature = temperature;
        this.device = device;
    }

}
