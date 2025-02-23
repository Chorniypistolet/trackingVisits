package com.test.trackingVisits.trackingVisits.repository;

import com.test.trackingVisits.trackingVisits.model.Doctor;
import com.test.trackingVisits.trackingVisits.model.Patient;
import com.test.trackingVisits.trackingVisits.model.Visit;
import com.test.trackingVisits.trackingVisits.repository.visit.VisitRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VisitRepositoryTest {
    @Autowired
    private VisitRepository visitRepository;

    @BeforeEach
    void beforeAll(
            @Autowired DataSource dataSource
    ) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            "database/create-doctors-table.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            "database/create-patients-table.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            "database/create-visits-table.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            "database/add-doctors-to-doctors-table.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            "database/add-patients-to-patients-table.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            "database/add-visits-to-visits-table.sql"));
        }
    }

    @AfterEach
    void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            "database/remove-visits-from-visits-table.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            "database/remove-patients-from-patients-table.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            "database/remove-doctors-from-doctors-table.sql"));
        }
    }

    @Test
    @DisplayName("Test findByPatientIdIn should return visits for existing patients")
    void testFindByPatientIdIn_WithExistingPatients_ShouldReturnVisits() {
        List<Visit> expected = createListOfVisits();

        List<Visit> actual = visitRepository.findByPatientIdIn(List.of(1L, 2L));

        assertFalse(actual.isEmpty());
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(1).getId(), actual.get(1).getId());
        assertEquals(expected.get(2).getDoctor().getId(), actual.get(2).getDoctor().getId());
    }

    @Test
    @DisplayName("Test findByPatientIdIn should return empty list for non-existing patients")
    void testFindByPatientIdIn_WithNonExistingPatients_ShouldReturnEmptyList() {
        List<Visit> result = visitRepository.findByPatientIdIn(List.of(99L, 100L));

        int expected = 0;
        int actual = result.size();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test findByPatientIdIn should return visits for a single existing patient")
    void testFindByPatientIdIn_WithSingleExistingPatient_ShouldReturnVisits() {
        Doctor doctorOne = createDoctor(1L, "Alice", "Brown", "UTC+2");
        Doctor doctorTwo = createDoctor(2L,"Bob", "Johnson", "UTC+2");
        Patient patient = createPatient(1L, "John", "Doe");
        Visit visitOne = createVisit(1L, "2025-02-21 12:00:00",
                "2025-02-21 13:00:00", doctorOne, patient);
        Visit visitTwo = createVisit(2L, "2025-03-21 12:00:00",
                "2025-03-21 13:00:00", doctorOne, patient);
        Visit visitThree = createVisit(3L, "2025-04-21 08:00:00",
                "2025-04-21 09:00:00", doctorOne, patient);
        Visit visitFour = createVisit(6L, "2025-04-21 10:00:00",
                "2025-04-21 12:00:00", doctorTwo, patient);
        List<Visit> expected = List.of(visitOne, visitTwo, visitThree, visitFour);

        List<Visit> actual = visitRepository.findByPatientIdIn(List.of(1L));

        assertFalse(actual.isEmpty());
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(1).getId(), actual.get(1).getId());
        assertEquals(expected.get(1).getDoctor().getId(), actual.get(1).getDoctor().getId());
        assertEquals(expected.get(1).getPatient().getId(), actual.get(1).getPatient().getId());

    }

    private Patient createPatient(Long id, String firstName, String secondName) {
        return new Patient(id, firstName, secondName);
    }

    private Visit createVisit(Long id, String startDateTime, String endDateTime, Doctor doctor, Patient patient) {
        return new Visit(id, startDateTime, endDateTime, doctor, patient);
    }

    private Doctor createDoctor(Long id, String firstName, String lastName, String timezone){
        return new Doctor(id, firstName, lastName, timezone);
    }

    private List<Visit> createListOfVisits() {
        Doctor doctorOne = createDoctor(1L, "Alice", "Brown", "UTC+2");
        Doctor doctorTwo = createDoctor(2L,"Bob", "Johnson", "UTC+2");
        Patient patient = createPatient(1L, "John", "Doe");
        Patient patientTwo = createPatient(2L, "Jane", "Doe");
        Visit visitOne = createVisit(1L, "2025-02-21 12:00:00",
                "2025-02-21 13:00:00", doctorOne, patient);
        Visit visitTwo = createVisit(2L, "2025-03-21 12:00:00",
                "2025-03-21 13:00:00", doctorOne, patient);
        Visit visitThree = createVisit(3L, "2025-04-21 08:00:00",
                "2025-04-21 09:00:00", doctorOne, patient);
        Visit visitFour = createVisit(4L, "2025-04-21 12:00:00",
                "2025-04-21 13:00:00", doctorOne, patientTwo);
        Visit visitFive = createVisit(5L, "2025-04-21 08:00:00",
                "2025-04-21 09:00:00", doctorTwo, patientTwo);
        Visit visitSix = createVisit(6L, "2025-04-21 10:00:00",
                "2025-04-21 12:00:00", doctorTwo, patient);
        return List.of(visitOne, visitTwo, visitThree, visitFour, visitFive, visitSix);
    }
}
