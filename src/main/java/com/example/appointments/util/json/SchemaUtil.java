package com.example.appointments.util.json;

import org.everit.json.schema.FormatValidator;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONTokener;

import java.io.InputStream;

public class SchemaUtil {

    public static Schema load(InputStream inputStream, FormatValidator... formatValidators) {
        SchemaLoader.SchemaLoaderBuilder schemaLoader = SchemaLoader.builder()
                .schemaJson(new JSONTokener(inputStream).nextValue())
                .enableOverrideOfBuiltInFormatValidators();

        for (FormatValidator formatValidator: formatValidators) {
            schemaLoader.addFormatValidator(formatValidator);
        }

        return schemaLoader.build().load().build();
    }
}
