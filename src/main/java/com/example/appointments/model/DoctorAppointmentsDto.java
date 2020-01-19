package com.example.appointments.model;

import lombok.Value;

import java.util.List;

@Value
public class DoctorAppointmentsDto {
    public String firstName;
    public String lastName;
    public List<AppointmentsByLocationDto> appointmentsByLocation;
}
