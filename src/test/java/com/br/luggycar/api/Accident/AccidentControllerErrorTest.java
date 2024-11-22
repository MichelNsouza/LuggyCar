package com.br.luggycar.api.Accident;

import com.br.luggycar.api.dtos.requests.AccidentRequest;
import com.br.luggycar.api.entities.Accident;
import com.br.luggycar.api.enums.accident.Severity;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.services.AccidentService;
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
import java.util.Date;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AccidentControllerErrorTest {

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

        accidentRequest = new AccidentRequest(Severity.MEDIUM, "Acidente com colisão", new java.util.Date());
    }

    @Test
    void testCreateAccidentWithInvalidSeverity() throws Exception {
        String invalidJson = "{ \"severity\": \"INVALID\", \"description\": \"Acidente com colisão\", \"registrationDate\": \"" + new java.util.Date() + "\"}";

        mockMvc.perform(post("/api/accident")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isInternalServerError());

    }

    @Test
    void testUpdateAccidentNotFound() throws Exception {
        Long id = 999L;
        AccidentRequest request = new AccidentRequest(Severity.MEDIUM, "Updated Description", new java.util.Date());

        when(accidentService.updateAccident(eq(id), any(AccidentRequest.class)))
                .thenThrow(new ResourceNotFoundException("Acidente não encontrado"));

        mockMvc.perform(put("/api/accident/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Acidente não encontrado"));
    }


    @Test
    void testDeleteAccidentNotFound() throws Exception {
        Long id = 999L;

        doThrow(new ResourceNotFoundException("Acidente não encontrado")).when(accidentService).deleteAccident(id);

        mockMvc.perform(delete("/api/accident/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Acidente não encontrado"));
    }


    @Test
    void testFindAccidentByIdNotFound() throws Exception {
        Long id = 999L;

        when(accidentService.findAccidentById(id)).thenThrow(new ResourceNotFoundException("Acidente não encontrado"));

        mockMvc.perform(get("/api/accident/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Acidente não encontrado"));
    }
}




