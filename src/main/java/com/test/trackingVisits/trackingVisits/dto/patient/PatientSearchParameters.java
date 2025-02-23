package com.test.trackingVisits.trackingVisits.dto.patient;

public record PatientSearchParameters(String search, String doctorIds) {

    public PatientSearchParameters {
        if (search == null) {
            search = "";
        }
        if (doctorIds == null) {
            doctorIds = "";
        }
    }
}
