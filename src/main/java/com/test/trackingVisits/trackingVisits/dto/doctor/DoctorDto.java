package com.test.trackingVisits.trackingVisits.dto.doctor;

import lombok.Data;

@Data
public class DoctorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private int totalPatients;
}
