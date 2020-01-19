package com.example.appointments.service.providers.example;

import com.example.appointments.util.json.DateTimeFormatValidator;
import com.example.appointments.util.json.SchemaUtil;
import org.everit.json.schema.Schema;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Configuration
public class ExampleConfiguration {

    @Bean
    public DateTimeFormatter exampleDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    @Bean
    public Schema exampleEntrySchema(
            @Value("classpath:${providers.example.schema}") Resource resource,
            @Qualifier("exampleDateTimeFormatter") DateTimeFormatter dateTimeFormatter) throws IOException {
        return SchemaUtil.load(
                resource.getInputStream(),
                new DateTimeFormatValidator(dateTimeFormatter));
    }
}
