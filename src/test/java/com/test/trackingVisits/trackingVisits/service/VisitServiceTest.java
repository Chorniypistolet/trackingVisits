package com.test.trackingVisits.trackingVisits.service;

import com.test.trackingVisits.trackingVisits.dto.visit.VisitRequestDto;
import com.test.trackingVisits.trackingVisits.model.Doctor;
import com.test.trackingVisits.trackingVisits.model.Patient;
import com.test.trackingVisits.trackingVisits.model.Visit;
import com.test.trackingVisits.trackingVisits.repository.doctor.DoctorRepository;
import com.test.trackingVisits.trackingVisits.repository.patient.PatientRepository;
import com.test.trackingVisits.trackingVisits.repository.visit.VisitRepository;
import com.test.trackingVisits.trackingVisits.service.impl.VisitServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VisitServiceTest {
    @Mock
    private VisitRepository visitRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private VisitServiceImpl visitService;

    @Test
    @DisplayName("Test create should save visit with valid patient and doctor")
    void testCreate_WithValidPatientAndDoctor_ShouldSaveVisit() {
        VisitRequestDto visitRequestDto = new VisitRequestDto();
        visitRequestDto.setStart("2025-02-21T12:00:00");
        visitRequestDto.setEnd("2025-02-21T13:00:00");
        visitRequestDto.setPatientId(1L);
        visitRequestDto.setDoctorId(1L);

        Patient patient = new Patient(1L, "John", "Doe");
        Doctor doctor = new Doctor(1L, "Alice", "Brown", "UTC+2");
        Visit visit = new Visit();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(visitRepository.save(any(Visit.class))).thenReturn(visit);

        visitService.create(visitRequestDto);

        verify(patientRepository, times(1)).findById(1L);
        verify(doctorRepository, times(1)).findById(1L);
        verify(visitRepository, times(1)).save(any(Visit.class));
    }

    @Test
    @DisplayName("Test create should throw EntityNotFoundException when patient is not found")
    void testCreate_WithNonExistingPatient_ShouldThrowException() {
        VisitRequestDto visitRequestDto = new VisitRequestDto();
        visitRequestDto.setStart("2025-02-21T12:00:00");
        visitRequestDto.setEnd("2025-02-21T13:00:00");
        visitRequestDto.setPatientId(1L);
        visitRequestDto.setDoctorId(1L);

        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> visitService.create(visitRequestDto));

        verify(patientRepository, times(1)).findById(1L);
        verify(doctorRepository, never()).findById(any(Long.class));
        verify(visitRepository, never()).save(any(Visit.class));
    }

    @Test
    @DisplayName("Test create should throw EntityNotFoundException when doctor is not found")
    void testCreate_WithNonExistingDoctor_ShouldThrowException() {
        VisitRequestDto visitRequestDto = new VisitRequestDto();
        visitRequestDto.setStart("2025-02-21T12:00:00");
        visitRequestDto.setEnd("2025-02-21T13:00:00");
        visitRequestDto.setPatientId(1L);
        visitRequestDto.setDoctorId(1L);

        Patient patient = new Patient(1L, "John", "Doe");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> visitService.create(visitRequestDto));

        verify(patientRepository, times(1)).findById(1L);
        verify(doctorRepository, times(1)).findById(1L);
        verify(visitRepository, never()).save(any(Visit.class));
    }
}
