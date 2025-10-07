package com.appointment.service.response;

import com.appointment.service.enums.AppointmentStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {

    private Long id;
    private String patientName;
    private String doctorName;
    private String department;
    private String appointmentDate;
    private AppointmentStatus status;
}
