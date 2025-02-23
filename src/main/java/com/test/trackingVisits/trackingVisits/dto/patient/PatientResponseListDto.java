package com.test.trackingVisits.trackingVisits.dto.patient;

import lombok.Data;

import java.util.List;

@Data
public class PatientResponseListDto {
    private List<PatientResponseDto> data;
    private long count;

    public PatientResponseListDto(List<PatientResponseDto> data, long count) {
        this.data = data;
        this.count = count;
    }
}
