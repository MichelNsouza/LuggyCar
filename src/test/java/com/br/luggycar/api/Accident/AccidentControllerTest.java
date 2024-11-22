package com.br.luggycar.api.Accident;

import com.br.luggycar.api.dtos.requests.AccidentRequest;
import com.br.luggycar.api.entities.Accident;
import com.br.luggycar.api.services.AccidentService;
import com.br.luggycar.api.enums.accident.Severity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
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

    private Accident accident;
    private AccidentRequest accidentRequest;

    @BeforeEach
    void setUp() {
        accident = new Accident();
        accident.setId(1L);
        accident.setDescription("Acidente com colisão");
        accident.setSeverity(Severity.MEDIUM);

        accidentRequest = new AccidentRequest(Severity.MEDIUM, "Acidente com colisão", null);
    }

    @Test
    void createAccident() throws Exception {
        when(accidentService.createAccident(any(AccidentRequest.class))).thenReturn(accident);

        mockMvc.perform(post("/api/accident")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accidentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accident.getId()))
                .andExpect(jsonPath("$.description").value(accident.getDescription()))
                .andExpect(jsonPath("$.severity").value(accident.getSeverity().toString()));
    }

    @Test
    void readAllAccidents() throws Exception {
        when(accidentService.readAllAccident()).thenReturn(List.of(accident));

        mockMvc.perform(get("/api/accident")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(accident.getId()))
                .andExpect(jsonPath("$[0].description").value(accident.getDescription()))
                .andExpect(jsonPath("$[0].severity").value(accident.getSeverity().toString()));
    }

    @Test
    void updateAccident() throws Exception {
        when(accidentService.updateAccident(eq(1L), any(AccidentRequest.class))).thenReturn(accident);

        mockMvc.perform(put("/api/accident/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accidentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accident.getId()))
                .andExpect(jsonPath("$.description").value(accident.getDescription()))
                .andExpect(jsonPath("$.severity").value(accident.getSeverity().toString()));
    }

    @Test
    void deleteAccident() throws Exception {
        when(accidentService.deleteAccident(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/accident/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void findAccidentById() throws Exception {
        when(accidentService.findAccidentById(1L)).thenReturn(accident);

        mockMvc.perform(get("/api/accident/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accident.getId()))
                .andExpect(jsonPath("$.description").value(accident.getDescription()))
                .andExpect(jsonPath("$.severity").value(accident.getSeverity().toString()));
    }
}



