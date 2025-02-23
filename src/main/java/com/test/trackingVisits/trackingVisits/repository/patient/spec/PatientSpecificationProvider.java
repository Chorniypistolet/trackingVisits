package com.test.trackingVisits.trackingVisits.repository.patient.spec;

import com.test.trackingVisits.trackingVisits.model.Patient;
import com.test.trackingVisits.trackingVisits.model.Visit;
import com.test.trackingVisits.trackingVisits.repository.SpecificationProvider;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PatientSpecificationProvider {

    @Component
    private static class FirstNameSpecification implements SpecificationProvider<Patient> {
        private static final String SEARCH_KEY = "search";

        @Override
        public String getKey() {
            return SEARCH_KEY;
        }

        @Override
        public Specification<Patient> getSpecification(String[] params) {
            return (root, query, criteriaBuilder) -> {
                String searchKeyword = params.length > 0 ? params[0] : "";
                return criteriaBuilder.like(root.get("firstName"), "%" + searchKeyword + "%");
            };
        }
    }

    @Component
    public static class DoctorIdSpecification implements SpecificationProvider<Patient> {
        @Override
        public String getKey() {
            return "doctorId";
        }

        @Override
        public Specification<Patient> getSpecification(String[] values) {
            List<Long> doctorIds = Arrays.stream(values)
                    .map(Long::parseLong)
                    .toList();

            return (root, query, criteriaBuilder) -> {
                if (doctorIds.isEmpty()) {
                    return criteriaBuilder.conjunction();
                }
                Join<Patient, Visit> visitsJoin = root.join("visits");
                return visitsJoin.get("doctor").get("id").in(doctorIds);
            };
        }
    }
}
