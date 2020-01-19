package com.example.appointments.util.json;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonSchemaValidator {
    private final Schema schema;

    public JsonSchemaValidator(Schema schema) {
        this.schema = schema;
    }

    public List<String> validate(Object jsonObject) {
        try {
            schema.validate(jsonObject);

            return Collections.emptyList();
        } catch (ValidationException e) {
            List<String> errors = new ArrayList<>();
            collectErrors(e, errors);
            return errors;
        }
    }

    private static void collectErrors(ValidationException e, List<String> errors) {
        errors.add(e.getMessage());
        for (ValidationException exception : e.getCausingExceptions()) {
            collectErrors(exception, errors);
        }
    }
}
