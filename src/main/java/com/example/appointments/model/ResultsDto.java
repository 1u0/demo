package com.example.appointments.model;

import lombok.Value;

import java.util.List;

@Value
public class ResultsDto<T> {
    List<T> results;
    List<ErrorDto> errors;
}
