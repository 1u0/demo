package com.example.appointments.util.json;

import org.everit.json.schema.FormatValidator;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class DateTimeFormatValidator implements FormatValidator {

    private final DateTimeFormatter formatter;

    public DateTimeFormatValidator(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public Optional<String> validate(String value) {
        try {
            formatter.parse(value);

            return Optional.empty();
        } catch (DateTimeParseException e) {
            return Optional.of(e.getMessage());
        }
    }

    @Override
    public String formatName() {
        return "date-time";
    }
}
