package com.test.trackingVisits.trackingVisits.repository.patient;

import com.test.trackingVisits.trackingVisits.model.Patient;
import com.test.trackingVisits.trackingVisits.repository.SpecificationProvider;
import com.test.trackingVisits.trackingVisits.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PatientSpecificationProviderManager implements SpecificationProviderManager<Patient> {
    private final List<SpecificationProvider<Patient>> patientSpecificationProviders;

    @Override
    public SpecificationProvider<Patient> getSpecificationProvider(String key) {
        return patientSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cant find correct"
                        + " specification provider for key " + key));
    }
}
