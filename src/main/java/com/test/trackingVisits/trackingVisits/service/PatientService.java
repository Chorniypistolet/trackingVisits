package com.test.trackingVisits.trackingVisits.service;

import com.test.trackingVisits.trackingVisits.dto.patient.PatientResponseListDto;
import org.springframework.stereotype.Service;

@Service
public interface PatientService {

    PatientResponseListDto findByParameters(int page, int size, String search, String doctorIds);
}
