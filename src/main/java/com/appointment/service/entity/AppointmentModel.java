package com.appointment.service.entity;

import com.appointment.service.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@Setter
@Table(name = "appointments")
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "patientName")
    private String patientName;

    @Column(name = "doctorName")
    private String doctorName;

    @Column(name = "department")
    private String department;

    @Column(name = "appointmentDate")
    private String appointmentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AppointmentStatus status;
}
