package com.br.luggycar.api.Accident;

import com.br.luggycar.api.dtos.requests.AccidentRequest;
import com.br.luggycar.api.entities.Accident;
import com.br.luggycar.api.enums.accident.Severity;
import com.br.luggycar.api.services.AccidentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AccidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccidentService accidentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateAccident() throws Exception {
        AccidentRequest request = new AccidentRequest(
                Severity.HIGH,
                "Test Accident Description",
                new Date()
        );

        Accident response = new Accident(
                1L,
                Severity.HIGH,
                "Test Accident Description",
                new Date()
        );

        when(accidentService.createAccident(any(AccidentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/accident")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.description").value(response.getDescription()));
    }

    @Test
    public void testReadAllAccidents() throws Exception {
        Accident response1 = new Accident(
                1L,
                Severity.LOW,
                "Description 1",
                new Date()
        );
        Accident response2 = new Accident(
                2L,
                Severity.MEDIUM,
                "Description 2",
                new Date()
        );
        List<Accident> accidents = Arrays.asList(response1, response2);

        when(accidentService.readAllAccident()).thenReturn(accidents);

        mockMvc.perform(get("/api/accident")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(accidents.size()));
    }

    @Test
    public void testUpdateAccident() throws Exception {
        Long id = 1L;
        AccidentRequest request = new AccidentRequest(
                Severity.MEDIUM,
                "Updated Description",
                new Date()
        );

        Accident response = new Accident(
                id,
                Severity.MEDIUM,
                "Updated Description",
                new Date()
        );

        when(accidentService.updateAccident(any(Long.class), any(AccidentRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/accident/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.description").value(response.getDescription()));
    }

    @Test
    public void testDeleteAccident() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/accident/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testFindAccidentById() throws Exception {
        Long id = 1L;
        Accident response = new Accident(
                1L,
                Severity.HIGH,
                "Test Accident Description",
                new Date()
        );

        when(accidentService.findAccidentById(any(Long.class))).thenReturn(response);

        mockMvc.perform(get("/api/accident/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.description").value(response.getDescription()));
    }
}


