package com.test.trackingVisits.trackingVisits.service.impl;

import com.test.trackingVisits.trackingVisits.dto.patient.PatientResponseDto;

import com.test.trackingVisits.trackingVisits.dto.patient.PatientResponseListDto;
import com.test.trackingVisits.trackingVisits.dto.patient.PatientSearchParameters;
import com.test.trackingVisits.trackingVisits.dto.visit.VisitDto;
import com.test.trackingVisits.trackingVisits.mapper.PatientMapper;
import com.test.trackingVisits.trackingVisits.mapper.VisitMapper;
import com.test.trackingVisits.trackingVisits.model.Patient;
import com.test.trackingVisits.trackingVisits.model.Visit;
import com.test.trackingVisits.trackingVisits.repository.SpecificationBuilder;
import com.test.trackingVisits.trackingVisits.repository.patient.PatientRepository;
import com.test.trackingVisits.trackingVisits.repository.visit.VisitRepository;
import com.test.trackingVisits.trackingVisits.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;
    private final SpecificationBuilder<Patient> specificationBuilder;
    private final PatientMapper patientMapper;
    private final VisitMapper visitMapper;

    @Override
    public PatientResponseListDto findByParameters(int page, int size, String search, String doctorIds) {
        Pageable pageable = PageRequest.of(page, size);
        PatientSearchParameters searchParameters = new PatientSearchParameters(search, doctorIds);
        Specification<Patient> specification = specificationBuilder.build(searchParameters);
        Page<Patient> patientsPage = patientRepository.findAll(specification, pageable);
        List<Patient> patients = patientsPage.getContent();

        List<Long> patientIds = patients.stream().map(Patient::getId).toList();
        List<Visit> allVisits = visitRepository.findByPatientIdIn(patientIds);

        Map<Long, List<Visit>> visitsByPatient = allVisits.stream()
                .collect(Collectors.groupingBy(v -> v.getPatient().getId()));

        Map<Long, List<VisitDto>> visitDtosByPatient = visitsByPatient.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(visitMapper::toVisitDto)
                                .toList()
                ));

        List<PatientResponseDto> patientDtos = patients.stream()
                .map(patient -> patientMapper.toPatientResponseDto(
                        patient, visitDtosByPatient.getOrDefault(patient.getId(), List.of())))
                .toList();

        return patientMapper.toPatientResponseListDto(patientDtos, patientsPage.getTotalElements());
    }
}
