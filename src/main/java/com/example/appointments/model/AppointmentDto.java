package com.example.appointments.model;

import lombok.Value;

import java.time.Instant;

@Value
public class AppointmentDto {
    public String appointmentId;
    public Instant startDateTime;
    public ServiceDto service;
}
