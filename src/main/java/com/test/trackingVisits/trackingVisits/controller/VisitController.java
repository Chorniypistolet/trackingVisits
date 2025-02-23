package com.test.trackingVisits.trackingVisits.controller;

import com.test.trackingVisits.trackingVisits.dto.visit.VisitRequestDto;
import com.test.trackingVisits.trackingVisits.service.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {
    private final VisitService visitService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createVisit(@RequestBody @Valid VisitRequestDto visitRequestDto) {
        visitService.create(visitRequestDto);
    }
}
