package com.example.appointments.service.providers.example;

import com.example.appointments.model.DoctorAppointmentsDto;
import com.example.appointments.model.ErrorDto;
import com.example.appointments.model.ErrorType;
import com.example.appointments.model.ResultsDto;
import com.example.appointments.util.JsonUtil;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;

@SpringBootTest
class ExampleAppointmentsServiceTest {
    @TestConfiguration
    static class TestBeans {
        @Bean
        @Primary
        public ExampleAppointmentsGateway mockGateway() {
            return Mockito.mock(ExampleAppointmentsGateway.class);
        }

        @Bean
        @Primary
        public ExampleAppointmentsMapper mockMapper() {
            return Mockito.mock(ExampleAppointmentsMapper.class);
        }
    }

    @Autowired
    private ExampleAppointmentsGateway mockGateway;

    @Autowired
    private ExampleAppointmentsMapper mockMapper;

    @Autowired
    private ExampleAppointmentsService service;

    @BeforeEach
    void setup() {
        Mockito.reset(mockGateway, mockMapper);
    }

    @Test
    public void exceptionFromGatewayIsPropagated() {
        Throwable exception = new IllegalArgumentException("some random exception");
        Mockito.when(mockGateway.getAppointments()).thenThrow(exception);

        assertThrows(exception.getClass(), () -> service.getAppointments());
    }

    @Test
    public void nonArrayJsonFromProviderApiIsReturnedAsError() {
        Object response = new JSONObject().put("foo", 42);
        Mockito.when(mockGateway.getAppointments()).thenReturn(response);

        ResultsDto<DoctorAppointmentsDto> results = service.getAppointments();
        assertThat(results.getResults(), is(empty()));
        assertThat(results.getErrors(), contains(
                new ErrorDto(
                        ErrorType.INVALID_RESPONSE,
                        Collections.singletonList("expected array as root element"))
        ));
    }

    @Test
    public void entriesWithInvalidSchemaAreFilteredAndReportedAsErrors() throws IOException {
        Mockito.when(mockGateway.getAppointments())
                .thenReturn(JsonUtil.loadJson("samples/providers/example/sample.errors.json"));
        List<DoctorAppointmentsDto> appointments = appointments();
        Mockito.when(mockMapper.map(anyList()))
                .thenReturn(appointments);

        ResultsDto<DoctorAppointmentsDto> results = service.getAppointments();
        assertThat(results.getResults(), is(equalTo(appointments)));
        assertThat(results.getErrors(), contains(
                new ErrorDto(
                        ErrorType.ELEMENT_SCHEMA_VIOLATION,
                        Arrays.asList(
                                "#/doctor: required key [lastName] not found",
                                "#/durationInMinutes: -5 is not greater or equal to 0",
                                "#/time: Text '2020-01-16' could not be parsed at index 10"
                        ))
        ));
    }

    private List<DoctorAppointmentsDto> appointments() {
        EasyRandom easyRandom = new EasyRandom(new EasyRandomParameters()
                .collectionSizeRange(0, 10));
        return easyRandom.objects(DoctorAppointmentsDto.class, 3).collect(Collectors.toList());
    }
}