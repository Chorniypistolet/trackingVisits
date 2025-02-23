package com.test.trackingVisits.trackingVisits.dto.visit;

import com.test.trackingVisits.trackingVisits.dto.doctor.DoctorDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitDto {
    private Long id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private DoctorDto doctor;

}
