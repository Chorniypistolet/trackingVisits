package com.test.trackingVisits.trackingVisits.repository.patient;

import com.test.trackingVisits.trackingVisits.dto.patient.PatientSearchParameters;
import com.test.trackingVisits.trackingVisits.model.Patient;
import com.test.trackingVisits.trackingVisits.model.Visit;
import com.test.trackingVisits.trackingVisits.repository.SpecificationBuilder;
import com.test.trackingVisits.trackingVisits.repository.SpecificationProviderManager;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PatientSpecificationBuilder implements SpecificationBuilder<Patient> {
    private final SpecificationProviderManager<Patient> specificationProviderManager;
    @Override
    public Specification<Patient> build(PatientSearchParameters searchParameters) {
        Specification<Patient> patientSpecification = Specification.where(null);
        if (searchParameters.search() != null && !searchParameters.search().isEmpty()) {
            patientSpecification = patientSpecification.and(specificationProviderManager
                    .getSpecificationProvider("search")
                    .getSpecification(new String[]{searchParameters.search()}));
        }
        if (searchParameters.doctorIds() != null && !searchParameters.doctorIds().isEmpty()) {
            List<Long> doctorIds = Arrays.stream(searchParameters.doctorIds().split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            patientSpecification = patientSpecification.and((root, query, criteriaBuilder) -> {
                Subquery<Long> subquery = query.subquery(Long.class);
                var visitRoot = subquery.from(Visit.class);
                subquery.select(visitRoot.get("patient").get("id"))
                        .where(criteriaBuilder.and(
                                criteriaBuilder.equal(visitRoot.get("patient"), root),
                                visitRoot.join("doctor").get("id").in(doctorIds)
                        ));

                return criteriaBuilder.exists(subquery);
            });
        }
        return patientSpecification;
    }
}
