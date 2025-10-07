package com.appointment.service.controller;

import com.appointment.service.request.AppointmentRequest;
import com.appointment.service.response.AppointmentResponse;
import com.appointment.service.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    @PostMapping("/create")
    public AppointmentResponse create(@RequestBody AppointmentRequest request) {
        return service.create(request);
    }

    @GetMapping("/getAppointments")
    public List<AppointmentResponse> getAppointments() {
        return service.getAppointments();
    }

    @PutMapping("/update/{patientName}")
    public AppointmentResponse update(@PathVariable String patientName, @RequestBody AppointmentRequest request) {
        return service.update(patientName, request);
    }

    @PatchMapping("/updateStatusByPatientName/{patientName}/{newStatus}")
    public AppointmentResponse updateAppointmentStatus(@PathVariable String patientName, @PathVariable String newStatus) {
        return service.updateAppointmentStatus(patientName, newStatus);
    }

    @DeleteMapping("/deleteByPatientName/{patientName}")
    public String deleteByPatientName(@PathVariable String patientName) {
        return service.deleteByPatientName(patientName);
    }
}
