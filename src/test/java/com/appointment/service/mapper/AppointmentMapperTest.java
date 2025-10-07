package com.appointment.service.mapper;

import com.appointment.service.entity.AppointmentModel;
import com.appointment.service.enums.AppointmentStatus;
import com.appointment.service.request.AppointmentRequest;
import com.appointment.service.response.AppointmentResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AppointmentMapperTest {

    @Test
    void testToEntity(){
        AppointmentRequest request = AppointmentRequest.builder()
                .patientName("Hayathulla khan")
                .doctorName("DR.Saira")
                .department("AllInOne Doc")
                .appointmentDate("18/10/2025")
                .status(AppointmentStatus.SCHEDULED)
                .build();

        AppointmentModel model = AppointmentMapper.toEntity(request);

        assertThat(model).isNotNull();
        assertThat(model.getPatientName()).isEqualTo("Hayathulla khan");
        assertThat(model.getDoctorName()).isEqualTo("DR.Saira");
        assertThat(model.getDepartment()).isEqualTo("AllInOne Doc");
        assertThat(model.getAppointmentDate()).isEqualTo("18/10/2025");
        assertThat(model.getStatus()).isEqualTo(AppointmentStatus.SCHEDULED);
    }

    @Test
    void testToResponse() {
        AppointmentModel model = AppointmentModel.builder()
                .id(1L)
                .patientName("Hayathulla Khan")
                .doctorName("DR.Saira")
                .department("AllInOne Doc")
                .appointmentDate("18/10/2025")
                .status(AppointmentStatus.COMPLETED)
                .build();

        AppointmentResponse response = AppointmentMapper.toResponse(model);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getPatientName()).isEqualTo("Hayathulla Khan");
        assertThat(response.getDoctorName()).isEqualTo("DR.Saira");
        assertThat(response.getDepartment()).isEqualTo("AllInOne Doc");
        assertThat(response.getAppointmentDate()).isEqualTo("18/10/2025");
        assertThat(response.getStatus()).isEqualTo(AppointmentStatus.COMPLETED);
    }
}
