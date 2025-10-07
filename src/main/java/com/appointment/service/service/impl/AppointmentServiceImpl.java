package com.appointment.service.service.impl;

import com.appointment.service.entity.AppointmentModel;
import com.appointment.service.enums.AppointmentStatus;
import com.appointment.service.mapper.AppointmentMapper;
import com.appointment.service.repository.AppointmentRepository;
import com.appointment.service.request.AppointmentRequest;
import com.appointment.service.response.AppointmentResponse;
import com.appointment.service.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository repository;

    @Override
    public AppointmentResponse create(AppointmentRequest request) {
        AppointmentModel appointment = AppointmentMapper.toEntity(request);
        return AppointmentMapper.toResponse(repository.saveAndFlush(appointment));
    }

    @Override
    public List<AppointmentResponse> getAppointments() {
        return repository.findAll().stream()
                .map(AppointmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponse update(String patientName, AppointmentRequest request) {
        AppointmentModel appointmentModel = repository.findByPatientName(patientName)
                .orElseThrow(() -> new RuntimeException("Appointment not found for: " + patientName));
        appointmentModel.setDoctorName(request.getDoctorName());
        appointmentModel.setDepartment(request.getDepartment());
        appointmentModel.setAppointmentDate(request.getAppointmentDate());
        appointmentModel.setStatus(request.getStatus());
        return AppointmentMapper.toResponse(repository.saveAndFlush(appointmentModel));
    }

    @Override
    public AppointmentResponse updateAppointmentStatus(String patientName, String newStatus) {
        AppointmentModel appointmentModel = repository.findByPatientName(patientName)
                .orElseThrow(() -> new RuntimeException("Appointment not found for: " + patientName));
        appointmentModel.setStatus(AppointmentStatus.valueOf(newStatus.toUpperCase()));
        return AppointmentMapper.toResponse(repository.saveAndFlush(appointmentModel));
    }

    @Override
    public String deleteByPatientName(String patientName) {
        Optional<AppointmentModel> appointmentOpt = repository.findByPatientName(patientName);
        if (appointmentOpt.isPresent()) {
            repository.delete(appointmentOpt.get());
            return "Appointment deleted for patient: " + patientName;
        } else {
            return "No appointment found for patient: " + patientName;
        }
    }
}
