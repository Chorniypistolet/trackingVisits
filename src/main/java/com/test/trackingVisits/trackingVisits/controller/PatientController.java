package com.test.trackingVisits.trackingVisits.controller;

import com.test.trackingVisits.trackingVisits.dto.patient.PatientResponseListDto;
import com.test.trackingVisits.trackingVisits.service.PatientService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public PatientResponseListDto getAllPatientsByParameters(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) int size,
            @RequestParam(required = false) @Pattern(regexp = "^[a-zA-Z0-9 ]*$",
                    message = "Search parameter is invalid") String search,
            @RequestParam(required = false) String doctorIds) {
        return patientService.findByParameters(page, size, search, doctorIds);
    }
}
