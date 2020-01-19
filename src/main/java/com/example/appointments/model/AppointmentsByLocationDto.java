package com.example.appointments.model;

import lombok.Value;

import java.util.List;

@Value
public class AppointmentsByLocationDto {
    public String locationName;
    public List<AppointmentDto> appointments;
}
