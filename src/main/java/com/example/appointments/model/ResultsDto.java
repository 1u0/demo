package com.example.appointments.model;

import lombok.Value;

import java.util.List;

@Value
public class ResultsDto<T> {
    public List<T> results;
    public List<ErrorDto> errors;
}
