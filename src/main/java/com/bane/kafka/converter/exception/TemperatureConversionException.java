package com.bane.kafka.converter.exception;

public class TemperatureConversionException extends Throwable{

    public TemperatureConversionException(String message) {
        super(message);
    }

    public TemperatureConversionException(Throwable cause) {
        super(cause);
    }
}
