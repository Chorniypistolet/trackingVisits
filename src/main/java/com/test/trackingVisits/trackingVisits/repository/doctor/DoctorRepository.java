package com.test.trackingVisits.trackingVisits.repository.doctor;

import com.test.trackingVisits.trackingVisits.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
