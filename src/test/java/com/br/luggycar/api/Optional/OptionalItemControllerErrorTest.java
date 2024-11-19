package com.br.luggycar.api.Optional;

import com.br.luggycar.api.dtos.requests.Optional.OptionalItemRequest;
import com.br.luggycar.api.dtos.requests.Optional.OptionalQuantityRequest;
import com.br.luggycar.api.entities.OptionalItem;
import com.br.luggycar.api.exceptions.ResourceExistsException;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.services.OptionalItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class OptionalItemControllerErrorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OptionalItemService optionalItemService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long id;
    private OptionalItemRequest optionalItemRequest;

    @BeforeEach
    public void setUp() {
        id = 1L;
        optionalItemRequest = new OptionalItemRequest(
                "GPS",
                50.00,
                10
        );
    }

    @Test
    public void testCreateOptionalItemAlreadyExists() throws Exception {
        when(optionalItemService.createOptionalItem(optionalItemRequest))
                .thenThrow(new ResourceExistsException("O item opcional já existe."));

        mockMvc.perform(post("/api/optionalitem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(optionalItemRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O item opcional já existe."));
    }

    @Test
    public void testUpdateOptionalItemNotFound() throws Exception {
        when(optionalItemService.updateOptionalItem(id, optionalItemRequest))
                .thenThrow(new ResourceNotFoundException("O item opcional não foi encontrado."));

        mockMvc.perform(put("/api/optionalitem/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(optionalItemRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("O item opcional não foi encontrado."));
    }

    @Test
    public void testDeleteOptionalItemNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("O item opcional não foi encontrado."))
                .when(optionalItemService).deleteOptionalItem(id);

        mockMvc.perform(delete("/api/optionalitem/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("O item opcional não foi encontrado."));
    }

    @Test
    public void testFindOptionalItemByIdNotFound() throws Exception {
        when(optionalItemService.findOptionalItemById(id))
                .thenThrow(new ResourceNotFoundException("O item opcional não foi encontrado."));

        mockMvc.perform(get("/api/optionalitem/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("O item opcional não foi encontrado."));
    }
}


