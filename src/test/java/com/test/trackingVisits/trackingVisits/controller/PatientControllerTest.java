package com.test.trackingVisits.trackingVisits.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.trackingVisits.trackingVisits.dto.patient.PatientResponseListDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PatientControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @BeforeEach
    void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
    }

    @Test
    @DisplayName("Test getAllPatientsByParameters should return a list of patients with valid parameters")
    void testGetAllPatientsByParameters_WithValidParameters_ShouldReturnListOfPatients() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/patients")
                        .param("page", "0")
                        .param("size", "20")
                        .param("search", "John")
                        .param("doctorIds", "1,2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        PatientResponseListDto actualResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                PatientResponseListDto.class);

        assertNotNull(actualResponse);
    }

    @Test
    @DisplayName("Test getAllPatientsByParameters should return an empty list when no patients found")
    void testGetAllPatientsByParameters_WithNoPatientsFound_ShouldReturnEmptyList() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/patients")
                        .param("page", "0")
                        .param("size", "20")
                        .param("search", "NonExistentSearch")
                        .param("doctorIds", "3,4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        assertNotNull(jsonResponse);
    }

    @Test
    @DisplayName("Test getAllPatientsByParameters should return a bad request status for invalid parameters")
    void testGetAllPatientsByParameters_WithInvalidParameters_ShouldReturnBadRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/patients")
                        .param("page", "0")
                        .param("size", "20")
                        .param("search", "!@#$%^&*()")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        assertNotNull(jsonResponse);
    }
}
