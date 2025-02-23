package com.test.trackingVisits.trackingVisits.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@ToString
@Table(name = "visits", uniqueConstraints = @UniqueConstraint(columnNames = {"doctor_id", "startDateTime"}))
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    public Visit(Long id, String startDateTime, String endDateTime, Doctor doctor, Patient patient) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.id = id;
        this.startDateTime = LocalDateTime.parse(startDateTime, formatter);
        this.endDateTime = LocalDateTime.parse(endDateTime, formatter);
        this.patient = patient;
        this.doctor = doctor;
    }

    public Visit(){
    }
}
