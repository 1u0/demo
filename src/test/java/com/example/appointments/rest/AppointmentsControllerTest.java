package com.example.appointments.rest;

import com.example.appointments.model.DoctorAppointmentsDto;
import com.example.appointments.model.ErrorDto;
import com.example.appointments.model.ResultsDto;
import com.example.appointments.service.AppointmentsService;
import com.example.appointments.util.json.SchemaUtil;
import org.everit.json.schema.Schema;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.stream.Collectors;

import static com.example.appointments.util.Matchers.matchesJsonSchema;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AppointmentsControllerTest {
    @TestConfiguration
    static class TestBeans {
        @Bean
        public Schema appointmentsResponseSchema(@Value("classpath:rest/appointments.schema.json") Resource resource) throws IOException {
            return SchemaUtil.load(resource.getInputStream());
        }
    }

    @Autowired
    private Schema appointmentsResponseSchema;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentsService appointmentsService;

    @BeforeEach
    public void setup() {
        Mockito.reset(appointmentsService);
    }

    @Test
    public void testResponse() throws Exception {
        ResultsDto<DoctorAppointmentsDto> result = appointmentsResults();

        Mockito.when(appointmentsService.getAppointments()).thenReturn(result);

        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().string(matchesJsonSchema(appointmentsResponseSchema)))
        ;
    }

    private ResultsDto<DoctorAppointmentsDto> appointmentsResults() {
        EasyRandom easyRandom = new EasyRandom(new EasyRandomParameters()
                .collectionSizeRange(0, 10));
        return new ResultsDto<>(
                easyRandom.objects(DoctorAppointmentsDto.class, 3).collect(Collectors.toList()),
                easyRandom.objects(ErrorDto.class, 5).collect(Collectors.toList()));
    }
}