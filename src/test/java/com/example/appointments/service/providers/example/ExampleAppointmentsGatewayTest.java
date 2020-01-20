package com.example.appointments.service.providers.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(ExampleAppointmentsGateway.class)
@AutoConfigureWebClient(registerRestTemplate = true)
class ExampleAppointmentsGatewayTest {

    @Autowired
    @Value("${providers.example.url}")
    private String providerApiUrl;

    @Autowired
    private ExampleAppointmentsGateway gateway;

    @Autowired
    private MockRestServiceServer mockServer;

    @Test
    public void gatewayMakesApiCallAndReturnsJson() {
        mockServer.expect(requestTo(providerApiUrl))
                .andRespond(withSuccess()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"foo\":[]}"));

        Object response = gateway.getAppointments();
        JSONObject expected = new JSONObject().put("foo", new JSONArray());
        assertTrue(expected.similar(response));
    }

    @Test
    public void gatewayThrowsResponseStatusExceptionIfResponseStatusIsNotOK() {
        mockServer.expect(requestTo(providerApiUrl))
                .andRespond(withStatus(HttpStatus.ACCEPTED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"foo\":[]}"));

        assertThrows(ResponseStatusException.class, () -> gateway.getAppointments());
    }
}