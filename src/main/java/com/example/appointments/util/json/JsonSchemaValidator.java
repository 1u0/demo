package com.example.appointments.util.json;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;

import java.util.Optional;

public class JsonSchemaValidator {
    private final Schema schema;

    public JsonSchemaValidator(Schema schema) {
        this.schema = schema;
    }

    public Optional<String> validate(Object jsonObject) {
        try {
            schema.validate(jsonObject);

            return Optional.empty();
        } catch (ValidationException e) {
            StringBuilder builder = new StringBuilder();
            collectErrors(e, builder);
            return Optional.of(builder.toString());
        }
    }

    private static void collectErrors(ValidationException e, StringBuilder builder) {
        builder.append(e.getMessage());
        for (ValidationException exception : e.getCausingExceptions()) {
            builder.append('\n');
            collectErrors(exception, builder);
        }
    }
}
