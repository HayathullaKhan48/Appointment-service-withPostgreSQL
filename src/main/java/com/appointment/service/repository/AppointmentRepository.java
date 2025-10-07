package com.appointment.service.repository;

import com.appointment.service.entity.AppointmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentModel, Long> {
    Optional<AppointmentModel> findByPatientName(String patientName);
}
