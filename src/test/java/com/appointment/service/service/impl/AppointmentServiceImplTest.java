package com.appointment.service.service.impl;

import com.appointment.service.entity.AppointmentModel;
import com.appointment.service.enums.AppointmentStatus;
import com.appointment.service.repository.AppointmentRepository;
import com.appointment.service.request.AppointmentRequest;
import com.appointment.service.response.AppointmentResponse;
import org.junit.jupiter.api.BeforeEach;
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

    static Stream<Object[]> createData() {
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
                }
        );
    }

    @ParameterizedTest
    @MethodSource("createData")
    void testCreate(AppointmentRequest request, AppointmentModel expectedModel) {
        when(repository.save(any())).thenReturn(expectedModel);

        AppointmentResponse actualResponse = service.create(request);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getId()).isEqualTo(expectedModel.getId());
        assertThat(actualResponse.getPatientName()).isEqualTo(expectedModel.getPatientName());
        assertThat(actualResponse.getDoctorName()).isEqualTo(expectedModel.getDoctorName());
        assertThat(actualResponse.getDepartment()).isEqualTo(expectedModel.getDepartment());
        assertThat(actualResponse.getAppointmentDate()).isEqualTo(expectedModel.getAppointmentDate());
        assertThat(actualResponse.getStatus()).isEqualTo(expectedModel.getStatus());

        verify(repository, times(1)).save(any());
    }

    static Stream<Object[]> getAppointmentsData() {
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

        return Stream.<Object[]>of(new Object[]{Arrays.asList(model1, model2)});
    }

    @ParameterizedTest
    @MethodSource("getAppointmentsData")
    void testGetAppointments(List<AppointmentModel> models) {
        when(repository.findAll()).thenReturn(models);

        List<AppointmentResponse> responses = service.getAppointments();

        assertThat(responses).hasSize(models.size());
        assertThat(responses.get(0).getPatientName()).isEqualTo(models.get(0).getPatientName());
        verify(repository, times(1)).findAll();
    }

    static Stream<Object[]> updateData() {
        AppointmentRequest updateRequest1 = AppointmentRequest.builder()
                .doctorName("Dr. Arzoo")
                .department("Dermatology")
                .appointmentDate("20/10/2025")
                .status(AppointmentStatus.CANCELLED)
                .build();

        AppointmentModel existing1 = AppointmentModel.builder()
                .id(1L)
                .patientName("Hayathulla Khan")
                .doctorName("Dr. Saira")
                .department("AllInOne Doc")
                .appointmentDate("20/10/2025")
                .status(AppointmentStatus.SCHEDULED)
                .build();

        return Stream.<Object[]>of(new Object[]{"Hayathulla Khan", updateRequest1, existing1});
    }

    @ParameterizedTest
    @MethodSource("updateData")
    void testUpdate(String patientName, AppointmentRequest request, AppointmentModel existingModel) {
        when(repository.findByPatientName(patientName)).thenReturn(Optional.of(existingModel));
        when(repository.save(any(AppointmentModel.class))).thenAnswer(i -> i.getArgument(0));

        AppointmentResponse response = service.update(patientName, request);

        assertThat(response.getDoctorName()).isEqualTo(request.getDoctorName());
        assertThat(response.getDepartment()).isEqualTo(request.getDepartment());
        assertThat(response.getStatus()).isEqualTo(request.getStatus());

        verify(repository, times(1)).findByPatientName(patientName);
        verify(repository, times(1)).save(existingModel);
    }

    static Stream<Object[]> updateStatusData() {
        AppointmentModel existingModel = AppointmentModel.builder()
                .id(1L)
                .patientName("Hayathulla Khan")
                .doctorName("Dr. Saira")
                .department("AllInOne Doc")
                .appointmentDate("20/10/2025")
                .status(AppointmentStatus.SCHEDULED)
                .build();

        return Stream.<Object[]>of(new Object[]{"Hayathulla Khan", "COMPLETED", existingModel});
    }

    @ParameterizedTest
    @MethodSource("updateStatusData")
    void testUpdateAppointmentStatus(String patientName, String status, AppointmentModel existingModel) {
        when(repository.findByPatientName(patientName)).thenReturn(Optional.of(existingModel));
        when(repository.save(any(AppointmentModel.class))).thenAnswer(i -> i.getArgument(0));

        AppointmentResponse response = service.updateAppointmentStatus(patientName, status);

        assertThat(response.getStatus()).isEqualTo(AppointmentStatus.COMPLETED);
        verify(repository, times(1)).findByPatientName(patientName);
        verify(repository, times(1)).save(existingModel);
    }

    static Stream<Object[]> deleteData() {
        AppointmentModel existing = AppointmentModel.builder()
                .id(1L)
                .patientName("Hayathulla Khan")
                .build();

        return Stream.of(
                new Object[]{"Hayathulla Khan", existing, "Appointment deleted for patient: Hayathulla Khan"},
                new Object[]{"Unknown", null, "No appointment found for patient: Unknown"}
        );
    }

    @ParameterizedTest
    @MethodSource("deleteData")
    void testDeleteByPatientName(String patientName, AppointmentModel existingModel, String expectedMsg) {
        Optional<AppointmentModel> existingOpt = Optional.ofNullable(existingModel);
        when(repository.findByPatientName(patientName)).thenReturn(existingOpt);

        String result = service.deleteByPatientName(patientName);

        assertThat(result).isEqualTo(expectedMsg);

        if (existingOpt.isPresent()) {
            verify(repository, times(1)).delete(existingOpt.get());
        } else {
            verify(repository, never()).delete(any(AppointmentModel.class));
        }
    }

}
