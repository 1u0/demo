package com.example.appointments.util;

import com.example.appointments.util.json.JsonSchemaValidator;
import org.everit.json.schema.Schema;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.json.JSONException;
import org.json.JSONTokener;

import java.util.Optional;

public class JsonSchemaMatcher extends TypeSafeDiagnosingMatcher<String> {
    private final JsonSchemaValidator schemaValidator;

    public JsonSchemaMatcher(Schema schema) {
        this.schemaValidator = new JsonSchemaValidator(schema);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("json schema compliant");
    }

    @Override
    protected boolean matchesSafely(String jsonString, Description mismatchDescription) {
        Object json;
        try {
            json = new JSONTokener(jsonString).nextValue();
        } catch (JSONException e) {
            mismatchDescription.appendText(e.getMessage());
            return false;
        }
        Optional<String> error = schemaValidator.validate(json);
        if (error.isPresent()) {
            mismatchDescription.appendText(error.get());
            return false;
        }
        return true;
    }
}
