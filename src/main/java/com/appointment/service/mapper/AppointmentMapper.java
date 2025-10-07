package com.appointment.service.mapper;

import com.appointment.service.entity.AppointmentModel;
import com.appointment.service.request.AppointmentRequest;
import com.appointment.service.response.AppointmentResponse;

public class AppointmentMapper {

    public static AppointmentModel toEntity(AppointmentRequest request){
        return AppointmentModel.builder()
                .patientName(request.getPatientName())
                .doctorName(request.getDoctorName())
                .department(request.getDepartment())
                .appointmentDate(request.getAppointmentDate())
                .status(request.getStatus())
                .build();
    }

    public static AppointmentResponse toResponse(AppointmentModel model){
        return AppointmentResponse.builder()
                .id(model.getId())
                .patientName(model.getPatientName())
                .doctorName(model.getDoctorName())
                .department(model.getDepartment())
                .appointmentDate(model.getAppointmentDate())
                .status(model.getStatus())
                .build();
    }
}
