package com.bane.kafka.converter.fahrenheit;

import com.bane.kafka.converter.exception.TemperatureConversionException;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CelsiusToFahrenheit {

    static double fahrenheit(String c) throws TemperatureConversionException {
        try {
            return Double.parseDouble(c) * 1.8 + 32;
        } catch (Exception e) {
            log.error("Error during temp conversion for value: " + c);
            throw new TemperatureConversionException(e);
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println(CelsiusToFahrenheit.fahrenheit("abc"));
        } catch (TemperatureConversionException e) {
            e.printStackTrace();
        }
    }
}
