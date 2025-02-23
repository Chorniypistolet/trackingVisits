package com.test.trackingVisits.trackingVisits.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String timezone;
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Visit> visits;

    public Doctor(Long id, String firstName, String lastName, String timezone){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.timezone = timezone;
    }

    public Doctor(){
    }
}
