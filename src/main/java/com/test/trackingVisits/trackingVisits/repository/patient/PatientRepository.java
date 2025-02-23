package com.test.trackingVisits.trackingVisits.repository.patient;

import com.test.trackingVisits.trackingVisits.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {
}
