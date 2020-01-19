package com.example.appointments.service.providers.example;

import com.example.appointments.model.AppointmentDto;
import com.example.appointments.model.AppointmentsByLocationDto;
import com.example.appointments.model.DoctorAppointmentsDto;
import com.example.appointments.model.ServiceDto;
import lombok.Value;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.appointments.util.StreamUtil.groupBy;
import static java.util.stream.Collectors.toList;

@Component
class ExampleAppointmentsMapper {
    private final DateTimeFormatter dateTimeFormatter;

    public ExampleAppointmentsMapper(@Qualifier("exampleDateTimeFormatter") DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    List<DoctorAppointmentsDto> map(List<JSONObject> appointments) {
        return groupBy(
                appointments,
                appointment -> mapDoctor(appointment.getJSONObject("doctor")),
                this::mapToDoctorAppointments);
    }

    private DoctorAppointmentsDto mapToDoctorAppointments(Doctor doctor, List<JSONObject> appointments) {
        return new DoctorAppointmentsDto(
                doctor.firstName,
                doctor.lastName,
                groupBy(
                        appointments,
                        // NB: assuming that location.name is unique identifier for a location;
                        //  and that location.timeZoneCode is important only for appointment's time zone.
                        appointment -> appointment.getJSONObject("location").getString("name"),
                        this::mapToAppointmentsByLocation)
        );
    }

    private AppointmentsByLocationDto mapToAppointmentsByLocation(String location, List<JSONObject> appointments) {
        return new AppointmentsByLocationDto(
                location,
                appointments.stream().map(this::mapAppointment).collect(toList()));
    }

    private AppointmentDto mapAppointment(JSONObject appointment) {
        return new AppointmentDto(
                appointment.getString("id"),
                mapDateTime(appointment.getString("time"), appointment.getJSONObject("location").getString("timeZoneCode")),
                mapService(appointment.getJSONObject("service")));
    }

    private Instant mapDateTime(String dateTime, String timeZoneCode) {
        ZoneId zoneId = ZoneId.of(timeZoneCode);
        return LocalDateTime.parse(dateTime, dateTimeFormatter).atZone(zoneId).toInstant();
    }

    private static ServiceDto mapService(JSONObject service) {
        return new ServiceDto(service.getString("name"), String.valueOf(service.getLong("price")));
    }

    private static Doctor mapDoctor(JSONObject doctor) {
        // NB: assuming that (firstName, lastName) is unique identifier for a doctor.
        return new Doctor(doctor.getString("firstName"), doctor.getString("lastName"));
    }

    @Value
    private static class Doctor {
        final String firstName;
        final String lastName;
    }
}
