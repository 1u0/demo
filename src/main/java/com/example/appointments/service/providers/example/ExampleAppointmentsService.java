package com.example.appointments.service.providers.example;

import com.example.appointments.model.DoctorAppointmentsDto;
import com.example.appointments.model.ErrorDto;
import com.example.appointments.model.ErrorType;
import com.example.appointments.model.ResultsDto;
import com.example.appointments.service.AppointmentsService;
import com.example.appointments.util.json.JsonSchemaValidator;
import org.everit.json.schema.Schema;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ExampleAppointmentsService implements AppointmentsService {
    private final ExampleAppointmentsGateway gateway;
    private final JsonSchemaValidator schemaValidator;
    private final ExampleAppointmentsMapper mapper;

    public ExampleAppointmentsService(
            ExampleAppointmentsGateway gateway,
            @Qualifier("exampleEntrySchema") Schema schema,
            ExampleAppointmentsMapper mapper) {
        this.gateway = gateway;
        this.schemaValidator = new JsonSchemaValidator(schema);
        this.mapper = mapper;
    }

    @Override
    public ResultsDto<DoctorAppointmentsDto> getAppointments() {
        return process(gateway.getAppointments());
    }

    private ResultsDto<DoctorAppointmentsDto> process(Object jsonResponse) {
        // Validation:
        //  * raw data level: proper json document, root object is an array containing entries;
        //  * entry level: schema validation, simple constrains validation (based on json schema);
        //  * collection level (TODO): id is unique, no overlap of appointments for the same doctor.
        List<ErrorDto> errors = new ArrayList<>();
        if (!(jsonResponse instanceof JSONArray)) {
            errors.add(new ErrorDto(
                    ErrorType.INVALID_RESPONSE,
                    Collections.singletonList("expected array as root element")));

            return new ResultsDto<>(
                    Collections.emptyList(),
                    errors);
        }

        List<JSONObject> appointments = new ArrayList<>();
        TreeSet<String> schemaErrors = new TreeSet<>();
        for (Object jsonObject : (JSONArray) jsonResponse) {
            Optional<String> error = schemaValidator.validate(jsonObject);
            if (error.isPresent()) {
                schemaErrors.add(error.get());
            } else {
                appointments.add((JSONObject) jsonObject);
            }
        }
        if (!schemaErrors.isEmpty()) {
            errors.add(new ErrorDto(ErrorType.ELEMENT_SCHEMA_VIOLATION, new ArrayList<>(schemaErrors)));
        }

        return new ResultsDto<>(mapper.map(appointments), errors);
    }
}
