package com.example.appointments.service.providers.example;

import com.example.appointments.model.AppointmentDto;
import com.example.appointments.model.AppointmentsByLocationDto;
import com.example.appointments.model.DoctorAppointmentsDto;
import com.example.appointments.model.ServiceDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

@SpringBootTest
class ExampleAppointmentsMapperTest {
    @Autowired
    private ExampleAppointmentsMapper mapper;

    @Test
    public void testGroupByDoctor() throws IOException {
        List<JSONObject> jsonObjects = json("samples/providers/example/sample1.json");

        List<DoctorAppointmentsDto> results = mapper.map(jsonObjects);
        assertThat(results, containsInAnyOrder(
                new DoctorAppointmentsDto(
                        "Bret",
                        "Ortiz",
                        listOf(
                                new AppointmentsByLocationDto(
                                        "Kansas City Care",
                                        listOf(
                                                new AppointmentDto(
                                                        "b11e0ba5-53d1-4d58-8c51-5e3c15bfb560",
                                                        Instant.parse("2020-01-16T09:44:00Z"),
                                                        Duration.ofMinutes(5),
                                                        new ServiceDto("Counselling", "2672"))
                                        )),
                                new AppointmentsByLocationDto(
                                        "KC Doctors",
                                        listOf(
                                                new AppointmentDto(
                                                        "8ec28eba-64ae-46ad-89bb-072daf06d7e2",
                                                        Instant.parse("2020-01-16T09:44:00Z"),
                                                        Duration.ofMinutes(10),
                                                        new ServiceDto("X-Ray", "8548"))
                                        ))
                        )),
                new DoctorAppointmentsDto(
                        "Salvador",
                        "Flatley",
                        listOf(
                                new AppointmentsByLocationDto(
                                        "Wichita rehabilitation center",
                                        listOf(
                                                new AppointmentDto(
                                                        "0059a3a5-069e-4283-b150-4c5196273a29",
                                                        Instant.parse("2020-01-16T15:47:00Z"),
                                                        Duration.ofMinutes(12),
                                                        new ServiceDto("Physiotherapy consultation", "9832"))))
                        ))
        ));
    }

    @Test
    public void testGroupByLocation() throws IOException {
        List<JSONObject> jsonObjects = json("samples/providers/example/sample2.json");

        List<DoctorAppointmentsDto> results = mapper.map(jsonObjects);
        assertThat(results, containsInAnyOrder(
                new DoctorAppointmentsDto(
                        "Betty",
                        "Mosciski",
                        listOf(
                                new AppointmentsByLocationDto(
                                        "Kansas City Medical Centre",
                                        listOf(
                                                new AppointmentDto(
                                                        "8b5df407-b495-435a-8204-4e735f666888",
                                                        Instant.parse("2020-01-16T11:01:00Z"),
                                                        Duration.ofMinutes(14),
                                                        new ServiceDto("X-Ray", "8548")),
                                                new AppointmentDto(
                                                        "ffbeacbd-4196-459a-bb30-e45c274613ee",
                                                        Instant.parse("2020-01-16T11:01:00Z"),
                                                        Duration.ofMinutes(10),
                                                        new ServiceDto("Discolored butthole flaps inspection", "3415"))
                                        )),
                                new AppointmentsByLocationDto(
                                        "Kansas City Care",
                                        listOf(
                                                new AppointmentDto(
                                                        "5c357963-f3fe-4541-86d5-b607be9739e9",
                                                        Instant.parse("2020-01-16T11:01:00Z"),
                                                        Duration.ofMinutes(9),
                                                        new ServiceDto("Physiotherapy consultation", "9832"))
                                        )),
                                new AppointmentsByLocationDto(
                                        "Wichita rehabilitation center",
                                        listOf(
                                                new AppointmentDto(
                                                        "90b41d63-c7a0-4e3c-9657-f6d1a6fd36ee",
                                                        Instant.parse("2020-01-16T11:01:00Z"),
                                                        Duration.ofMinutes(7),
                                                        new ServiceDto("Teeth cleaning", "6815"))
                                        ))
                        ))
        ));
    }

    public static List<JSONObject> json(String resourcePath) throws IOException {
        Object json = new JSONTokener(new ClassPathResource(resourcePath).getInputStream()).nextValue();
        List<JSONObject> results = new ArrayList<>();
        for (Object element : (JSONArray) json) {
            results.add((JSONObject) element);
        }
        return results;
    }

    private static <T> List<T> listOf(T... elements) {
        return Arrays.asList(elements);
    }
}