package com.test.trackingVisits.trackingVisits.service;

import com.test.trackingVisits.trackingVisits.dto.patient.PatientResponseDto;
import com.test.trackingVisits.trackingVisits.dto.patient.PatientResponseListDto;
import com.test.trackingVisits.trackingVisits.dto.patient.PatientSearchParameters;
import com.test.trackingVisits.trackingVisits.dto.visit.VisitDto;
import com.test.trackingVisits.trackingVisits.mapper.PatientMapper;
import com.test.trackingVisits.trackingVisits.mapper.VisitMapper;
import com.test.trackingVisits.trackingVisits.model.Doctor;
import com.test.trackingVisits.trackingVisits.model.Patient;
import com.test.trackingVisits.trackingVisits.model.Visit;
import com.test.trackingVisits.trackingVisits.repository.SpecificationBuilder;
import com.test.trackingVisits.trackingVisits.repository.patient.PatientRepository;
import com.test.trackingVisits.trackingVisits.repository.visit.VisitRepository;
import com.test.trackingVisits.trackingVisits.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {
    @Mock
    private PatientRepository patientRepository;

    @Mock
    private VisitRepository visitRepository;

    @Mock
    private SpecificationBuilder<Patient> specificationBuilder;

    @Mock
    private PatientMapper patientMapper;

    @Mock
    private VisitMapper visitMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    @DisplayName("Test findByParameters should return a list of PatientResponseDto with visits")
    void testFindByParameters_WithCorrectParameters_ShouldReturnListOfPatientResponseDto() {
        int page = 0;
        int size = 20;
        String search = "John";
        String doctorIds = "1,2";

        Pageable pageable = PageRequest.of(page, size);
        PatientSearchParameters searchParameters = new PatientSearchParameters(search, doctorIds);
        Specification<Patient> specification = mock(Specification.class);

        Patient patient = new Patient(1L, "John", "Doe");
        Doctor doctor = new Doctor(1L, "Alice", "Brown", "UTC+2");
        Visit visit = new Visit(1L, "2025-02-21 12:00:00",
                "2025-02-21 13:00:00", doctor, patient);
        VisitDto visitDto = new VisitDto();
        PatientResponseDto patientResponseDto = new PatientResponseDto();

        Page<Patient> patientsPage = new PageImpl<>(List.of(patient));

        when(specificationBuilder.build(searchParameters)).thenReturn(specification);
        when(patientRepository.findAll(specification, pageable)).thenReturn(patientsPage);
        when(visitRepository.findByPatientIdIn(List.of(1L))).thenReturn(List.of(visit));
        when(visitMapper.toVisitDto(visit)).thenReturn(visitDto);
        when(patientMapper.toPatientResponseDto(patient, List.of(visitDto))).thenReturn(patientResponseDto);
        when(patientMapper.toPatientResponseListDto(List.of(patientResponseDto), 1L))
                .thenReturn(new PatientResponseListDto(List.of(patientResponseDto), 1L));

        PatientResponseListDto actual = patientService.findByParameters(page, size, search, doctorIds);

        int expected = 1;

        assertEquals(expected, actual.getCount());
        assertEquals(expected, actual.getData().size());
        assertEquals(patientResponseDto, actual.getData().get(0));
    }

    @Test
    @DisplayName("Test findByParameters should return an empty list when no patients found")
    void testFindByParameters_WithNoPatientsFound_ShouldReturnEmptyList() {
        int page = 0;
        int size = 20;
        String search = "John";
        String doctorIds = "1,2";

        Pageable pageable = PageRequest.of(page, size);
        PatientSearchParameters searchParameters = new PatientSearchParameters(search, doctorIds);
        Specification<Patient> specification = mock(Specification.class);

        Page<Patient> emptyPatientsPage = new PageImpl<>(Collections.emptyList());

        when(specificationBuilder.build(searchParameters)).thenReturn(specification);
        when(patientRepository.findAll(specification, pageable)).thenReturn(emptyPatientsPage);
        when(patientMapper.toPatientResponseListDto(Collections.emptyList(), 0L)).thenReturn(new PatientResponseListDto(Collections.emptyList(), 0L));

        PatientResponseListDto actual = patientService.findByParameters(page, size, search, doctorIds);

        int expected = 0;
        assertEquals(expected, actual.getCount());
        assertEquals(expected, actual.getData().size());
    }

    @Test
    @DisplayName("Test findByParameters should throw an exception when invalid parameters are provided")
    void testFindByParameters_WithInvalidParameters_ShouldThrowException() {
        int page = -1;
        int size = 20;
        String search = null;
        String doctorIds = null;

        assertThrows(IllegalArgumentException.class, () -> patientService.findByParameters(page, size, search, doctorIds));
    }
}
