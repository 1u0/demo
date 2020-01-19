package com.example.appointments.util;

import org.everit.json.schema.Schema;
import org.hamcrest.Matcher;

public class Matchers {

    public static Matcher<String> matchesJsonSchema(Schema schema) {
        return new JsonSchemaMatcher(schema);
    }
}
