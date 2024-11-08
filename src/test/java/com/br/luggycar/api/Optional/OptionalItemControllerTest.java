package com.br.luggycar.api.Optional;

import com.br.luggycar.api.dtos.requests.OptionalItemRequest;
import com.br.luggycar.api.entities.OptionalItem;
import com.br.luggycar.api.services.OptionalItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class OptionalItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OptionalItemService optionalItemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateOptionalItem() throws Exception {
        OptionalItemRequest request = new OptionalItemRequest(
                "GPS",
                50.00,
                10
        );

        OptionalItem response = new OptionalItem(
                1L,
                "GPS",
                50.00,
                10
        );

        when(optionalItemService.createOptionalItem(any(OptionalItemRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/optionalitem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.name").value(response.getName()));
    }

    @Test
    public void testReadAllOptionalItem() throws Exception {
        OptionalItem response1 = new OptionalItem(
                1L,
                "mesa",
                50.00,
                10
        );

        OptionalItem response2 = new OptionalItem(
                2L,
                "mesa",
                30.00,
                5
        );

        List<OptionalItem> optionalItems = Arrays.asList(response1, response2);

        when(optionalItemService.readAllOptionalItem()).thenReturn(optionalItems);

        mockMvc.perform(get("/api/optionalitem")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(optionalItems.size()));
    }

    @Test
    public void testUpdateOptionalItem() throws Exception {
        Long id = 1L;
        OptionalItemRequest request = new OptionalItemRequest(
                "cadeira",
                55.00,
                8
        );

        OptionalItem response = new OptionalItem(
                id,
                "cadeira",
                55.00,
                8
        );

        when(optionalItemService.updateOptionalItem(any(Long.class), any(OptionalItemRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/optionalitem/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.name").value(response.getName()));
    }

//    @Test
//    public void testDeleteOptionalItem() throws Exception {
//        Long id = 1L;
//
//        when(optionalItemService.deleteOptionalItem(any(Long.class))).thenReturn(true);
//
//        mockMvc.perform(delete("/api/optionalitem/{id}", id)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.value").value(true));
//    }


    @Test
    public void testFindOptionalItemById() throws Exception {
        Long id = 1L;
        OptionalItem response = new OptionalItem(
                id,
                "projetor",
                50.00,
                10
        );

        when(optionalItemService.findOptionalItemById(any(Long.class))).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/optionalitem/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.name").value(response.getName()));
    }
}

