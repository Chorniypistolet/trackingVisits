package com.test.trackingVisits.trackingVisits.dto.visit;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VisitRequestDto {
    @NotNull
    private String start;
    @NotNull
    private String end;
    @NotNull
    private Long patientId;
    @NotNull
    private Long doctorId;
}
