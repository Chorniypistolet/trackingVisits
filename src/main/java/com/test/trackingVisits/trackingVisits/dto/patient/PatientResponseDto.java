package com.test.trackingVisits.trackingVisits.dto.patient;

import com.test.trackingVisits.trackingVisits.dto.visit.VisitDto;
import lombok.Data;

import java.util.List;

@Data
public class PatientResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private List<VisitDto> lastVisits;
}
