package com.example.appointments.service.providers.example;

import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ExampleAppointmentsGateway {
    private final RestTemplate rest;
    private final String providerApiUrl;

    public ExampleAppointmentsGateway(
            RestTemplate exampleRestTemplate,
            @Value("${providers.example.url}") String providerApiUrl) {
        this.rest = exampleRestTemplate;
        this.providerApiUrl = providerApiUrl;
    }

    /**
     * @return deserialized JSON object, returned from `GET endpoint` request
     */
    public Object getAppointments() {
        ResponseEntity<String> response = rest.getForEntity(providerApiUrl, String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new ResponseStatusException(response.getStatusCode(), "non OK response from the api provider");
        }
        return new JSONTokener(response.getBody()).nextValue();
    }
}
