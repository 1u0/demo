package com.example.appointments.model;

import lombok.Value;

import java.util.List;

@Value
public class ErrorDto {
    ErrorType type;
    List<String> message;
}
