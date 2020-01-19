package com.example.appointments.rest;

import com.example.appointments.model.ResultsDto;
import com.example.appointments.service.AppointmentsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentsController {
    private final AppointmentsService service;

    public AppointmentsController(AppointmentsService service) {
        this.service = service;
    }

    @GetMapping
    public ResultsDto getAppointments() {
        return service.getAppointments();
    }
}
