package com.appointment.service.service;

import com.appointment.service.request.AppointmentRequest;
import com.appointment.service.response.AppointmentResponse;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse create(AppointmentRequest request);
    List<AppointmentResponse> getAppointments();
    AppointmentResponse update(String patientName, AppointmentRequest request);
    AppointmentResponse updateAppointmentStatus(String patientName, String newStatus);
    String  deleteByPatientName(String patientName);
}
