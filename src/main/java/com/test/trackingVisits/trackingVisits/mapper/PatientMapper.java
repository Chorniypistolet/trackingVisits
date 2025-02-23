package com.test.trackingVisits.trackingVisits.mapper;

import com.test.trackingVisits.trackingVisits.config.MapperConfig;
import com.test.trackingVisits.trackingVisits.dto.patient.PatientResponseDto;
import com.test.trackingVisits.trackingVisits.dto.patient.PatientResponseListDto;
import com.test.trackingVisits.trackingVisits.dto.visit.VisitDto;
import com.test.trackingVisits.trackingVisits.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface PatientMapper {
    @Mapping(target = "data", source = "patients")
    @Mapping(target = "count", source = "count")
    PatientResponseListDto toPatientResponseListDto(List<PatientResponseDto> patients, long count);

    @Mapping(target = "lastVisits", source = "visits")
    PatientResponseDto toPatientResponseDto(Patient patient, List<VisitDto> visits);
}
