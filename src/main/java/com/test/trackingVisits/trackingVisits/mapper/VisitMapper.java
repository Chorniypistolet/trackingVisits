package com.test.trackingVisits.trackingVisits.mapper;

import com.test.trackingVisits.trackingVisits.config.MapperConfig;
import com.test.trackingVisits.trackingVisits.dto.visit.VisitDto;
import com.test.trackingVisits.trackingVisits.model.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface VisitMapper {
    @Mapping(target = "doctor", source = "doctor")
    VisitDto toVisitDto(Visit visit);
}
