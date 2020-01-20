package com.example.appointments.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;

import java.time.Duration;
import java.time.Instant;

@Value
public class AppointmentDto {
    public String appointmentId;
    public Instant startDateTime;
    @JsonIgnore
    Duration duration;
    public ServiceDto service;
}
