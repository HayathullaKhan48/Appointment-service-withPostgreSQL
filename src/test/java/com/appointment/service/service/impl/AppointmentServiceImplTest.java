package com.appointment.service.service.impl;

import com.appointment.service.entity.AppointmentModel;
import com.appointment.service.enums.AppointmentStatus;
import com.appointment.service.repository.AppointmentRepository;
import com.appointment.service.request.AppointmentRequest;
import com.appointment.service.response.AppointmentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AppointmentServiceImplTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private AppointmentRepository repository;

    private AppointmentServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new AppointmentServiceImpl(repository);
    }

    static Stream<Object[]> appointmentData() {
        return Stream.of(
                new Object[]{
                        AppointmentRequest.builder()
                                .patientName("Hayathulla Khan")
                                .doctorName("Dr. Saira")
                                .department("AllInOne Doc")
                                .appointmentDate("20/10/2025")
                                .status(AppointmentStatus.SCHEDULED)
                                .build(),
                        AppointmentModel.builder()
                                .id(1L)
                                .patientName("Hayathulla Khan")
                                .doctorName("Dr. Saira")
                                .department("AllInOne Doc")
                                .appointmentDate("20/10/2025")
                                .status(AppointmentStatus.SCHEDULED)
                                .build()
                },
                new Object[]{
                        AppointmentRequest.builder()
                                .patientName("Arzoo Khanam")
                                .doctorName("Dr. John")
                                .department("Cardiology")
                                .appointmentDate("25/11/2025")
                                .status(AppointmentStatus.SCHEDULED)
                                .build(),
                        AppointmentModel.builder()
                                .id(2L)
                                .patientName("Arzoo Khanam")
                                .doctorName("Dr. John")
                                .department("Cardiology")
                                .appointmentDate("25/11/2025")
                                .status(AppointmentStatus.SCHEDULED)
                                .build()
                },
                new Object[]{
                        AppointmentRequest.builder()
                                .patientName("Nayazi Khan")
                                .doctorName("Dr. Smith")
                                .department("Neurology")
                                .appointmentDate("15/12/2025")
                                .status(AppointmentStatus.COMPLETED)
                                .build(),
                        AppointmentModel.builder()
                                .id(3L)
                                .patientName("Nayazi Khan")
                                .doctorName("Dr. Smith")
                                .department("Neurology")
                                .appointmentDate("15/12/2025")
                                .status(AppointmentStatus.COMPLETED)
                                .build()
                }
        );
    }

    @ParameterizedTest
    @MethodSource("appointmentData")
    void testCreate(AppointmentRequest request,
                    AppointmentModel expectedModel) {
        when(repository.save(any())).thenReturn(expectedModel);

        AppointmentResponse actualResponse = service.create(request);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getId()).isEqualTo(expectedModel.getId());
        assertThat(actualResponse.getPatientName()).isEqualTo(expectedModel.getPatientName());
        assertThat(actualResponse.getDoctorName()).isEqualTo(expectedModel.getDoctorName());
        assertThat(actualResponse.getDepartment()).isEqualTo(expectedModel.getDepartment());
        assertThat(actualResponse.getAppointmentDate()).isEqualTo(expectedModel.getAppointmentDate());
        assertThat(actualResponse.getStatus()).isEqualTo(expectedModel.getStatus());
    }

    @Test
    void testGetAppointments() {
        AppointmentModel model1 = AppointmentModel.builder()
                .id(1L)
                .patientName("Hayathulla Khan")
                .doctorName("DR. Saira")
                .department("AllInOne Doc")
                .appointmentDate("20/10/2025")
                .status(AppointmentStatus.SCHEDULED)
                .build();

        AppointmentModel model2 = AppointmentModel.builder()
                .id(2L)
                .patientName("Arzoo Khanam")
                .doctorName("DR. Saira")
                .department("AllInOne Doc")
                .appointmentDate("21/10/2025")
                .status(AppointmentStatus.SCHEDULED)
                .build();

        when(repository.findAll()).thenReturn(Arrays.asList(model1, model2));
        List<AppointmentResponse> responses = service.getAppointments();

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getPatientName()).isEqualTo("Hayathulla Khan");
        assertThat(responses.get(1).getPatientName()).isEqualTo("Arzoo Khanam");

        verify(repository, times(1)).findAll();
    }

    @Test
    void testUpdate() {
        AppointmentRequest request = AppointmentRequest.builder()
                .doctorName("Dr. Arzoo")
                .department("Dermatology")
                .appointmentDate("20/10/2025")
                .status(AppointmentStatus.CANCELLED)
                .build();

        AppointmentModel existingModel = AppointmentModel.builder()
                .id(1L)
                .patientName("Hayathulla Khan")
                .doctorName("Dr. Saira")
                .department("AllInOne Doc")
                .appointmentDate("20/10/2025")
                .status(AppointmentStatus.SCHEDULED)
                .build();

        when(repository.findByPatientName("Hayathulla Khan")).thenReturn(Optional.of(existingModel));
        when(repository.save(any(AppointmentModel.class))).thenAnswer(i -> i.getArgument(0));

        AppointmentResponse response = service.update("Hayathulla Khan", request);

        assertThat(response.getDoctorName()).isEqualTo("Dr. Arzoo");
        assertThat(response.getDepartment()).isEqualTo("Dermatology");
        assertThat(response.getStatus()).isEqualTo(AppointmentStatus.CANCELLED);

        verify(repository, times(1)).findByPatientName("Hayathulla Khan");
        verify(repository, times(1)).save(existingModel);
    }

    @Test
    void testUpdateAppointmentStatus() {
        AppointmentModel existingModel = AppointmentModel.builder()
                .id(1L)
                .patientName("Hayathulla Khan")
                .doctorName("Dr. Saira")
                .department("AllInOne Doc")
                .appointmentDate("20/10/2025")
                .status(AppointmentStatus.SCHEDULED)
                .build();

        when(repository.findByPatientName(anyString())).thenReturn(Optional.of(existingModel));
        when(repository.save(any(AppointmentModel.class))).thenAnswer(i -> i.getArgument(0));

        AppointmentResponse response = service.updateAppointmentStatus("Hayathulla Khan", "COMPLETED");

        assertThat(response.getStatus()).isEqualTo(AppointmentStatus.COMPLETED);

        verify(repository, times(1)).findByPatientName(anyString());
        verify(repository, times(1)).save(existingModel);
    }

    @Test
    void testDeleteByPatientName_found() {
        AppointmentModel existingModel = AppointmentModel.builder()
                .id(1L)
                .patientName("Hayathulla Khan")
                .build();

        when(repository.findByPatientName("Hayathulla Khan")).thenReturn(Optional.of(existingModel));

        String result = service.deleteByPatientName("Hayathulla Khan");

        assertThat(result).isEqualTo("Appointment deleted for patient: Hayathulla Khan");
        verify(repository, times(1)).delete(existingModel);
    }

    @Test
    void testDeletedByPatientName_NotFound(){
        when(repository.findByPatientName("Unknown Patient Name")).thenReturn(Optional.empty());

        String result = service.deleteByPatientName("Unknown Patient Name");

        assertThat(result).isEqualTo("No appointment found for patient: Unknown Patient Name");
        verify(repository, never()).delete(any(AppointmentModel.class));
    }
}