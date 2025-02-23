package com.test.trackingVisits.trackingVisits.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.trackingVisits.trackingVisits.dto.visit.VisitRequestDto;
import com.test.trackingVisits.trackingVisits.service.VisitService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VisitControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @Mock
    private VisitService visitService;

    @InjectMocks
    private VisitController visitController;

    @BeforeEach
    void beforeAll(
            @Autowired WebApplicationContext applicationContext,
            @Autowired DataSource dataSource
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
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
    @DisplayName("Test createVisit should return Created status with valid request")
    void testCreateVisit_WithValidRequest_ShouldReturnCreatedStatus() throws Exception {
        VisitRequestDto validVisitRequestDto = new VisitRequestDto();
        validVisitRequestDto.setStart("2025-02-21T12:00:00");
        validVisitRequestDto.setEnd("2025-02-21T13:00:00");
        validVisitRequestDto.setPatientId(1L);
        validVisitRequestDto.setDoctorId(1L);

        doNothing().when(visitService).create(validVisitRequestDto);

        mockMvc.perform(post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validVisitRequestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Test createVisit should return BadRequest status with invalid request")
    void testCreateVisit_WithInvalidRequest_ShouldReturnBadRequestStatus() throws Exception {
        VisitRequestDto invalidVisitRequestDto = new VisitRequestDto();
        invalidVisitRequestDto.setStart("invalid-date-format");

        mockMvc.perform(post("/visits")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVisitRequestDto)))
                .andExpect(status().isBadRequest());
    }
}
