package com.example.appointments.service;

import com.example.appointments.model.DoctorAppointmentsDto;
import com.example.appointments.model.ResultsDto;

public interface AppointmentsService {

    ResultsDto<DoctorAppointmentsDto> getAppointments();
}
