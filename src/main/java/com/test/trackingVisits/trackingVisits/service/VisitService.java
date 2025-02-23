package com.test.trackingVisits.trackingVisits.service;

import com.test.trackingVisits.trackingVisits.dto.visit.VisitRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface VisitService {
    void create(VisitRequestDto visitRequestDto);
}
