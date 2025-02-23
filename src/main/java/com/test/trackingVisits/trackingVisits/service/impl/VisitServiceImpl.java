package com.test.trackingVisits.trackingVisits.service.impl;

import com.test.trackingVisits.trackingVisits.dto.visit.VisitRequestDto;
import com.test.trackingVisits.trackingVisits.model.Doctor;
import com.test.trackingVisits.trackingVisits.model.Patient;
import com.test.trackingVisits.trackingVisits.model.Visit;
import com.test.trackingVisits.trackingVisits.repository.doctor.DoctorRepository;
import com.test.trackingVisits.trackingVisits.repository.patient.PatientRepository;
import com.test.trackingVisits.trackingVisits.repository.visit.VisitRepository;
import com.test.trackingVisits.trackingVisits.service.VisitService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class VisitServiceImpl implements VisitService {
    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    @Override
    public void create(VisitRequestDto visitRequestDto) {
        Visit visit = new Visit();
        visit.setStartDateTime(LocalDateTime.parse(visitRequestDto.getStart()));
        visit.setEndDateTime(LocalDateTime.parse(visitRequestDto.getEnd()));

        Patient patient = patientRepository.findById(visitRequestDto.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
        Doctor doctor = doctorRepository.findById(visitRequestDto.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        visit.setPatient(patient);
        visit.setDoctor(doctor);

        visitRepository.save(visit);
    }
}
