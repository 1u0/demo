package com.example.appointments.model;

import lombok.Value;

import java.util.List;

@Value
public class ErrorDto {
    public String type;
    public List<String> message;
}
