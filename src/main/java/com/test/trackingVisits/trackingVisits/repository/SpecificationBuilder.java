package com.test.trackingVisits.trackingVisits.repository;

import com.test.trackingVisits.trackingVisits.dto.patient.PatientSearchParameters;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(PatientSearchParameters searchParameters);
}
