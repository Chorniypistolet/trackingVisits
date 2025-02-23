package com.test.trackingVisits.trackingVisits.repository.visit;

import com.test.trackingVisits.trackingVisits.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByPatientIdIn(List<Long> patientIds);
}
