package com.br.luggycar.api.Category;

import com.br.luggycar.api.dtos.requests.CategoryRequest;
import com.br.luggycar.api.dtos.response.CategoryResponse;
import com.br.luggycar.api.services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long id;
    private CategoryRequest categoryRequest;
    private CategoryResponse categoryResponse;

    @BeforeEach
    public void setUp() {
        id = 1L;

        categoryRequest = new CategoryRequest(
                "Hatch",
                "Um carro pequeno",
                Collections.emptyList()
        );

        categoryResponse = new CategoryResponse(
                id,
                "Hatch",
                "Um carro pequeno",
                Collections.emptyList()
        );
    }

    @Test
    public void testCreateCategory() throws Exception {
        Mockito.when(categoryService.createCategory(any(CategoryRequest.class))).thenReturn(categoryResponse);

        String response = objectMapper.writeValueAsString(categoryResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    public void testReadAllCategories() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/category")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateCategory() throws Exception {
        CategoryResponse updatedResponse = new CategoryResponse(
                id,
                "Hatch2",
                "Um carro legalzão",
                Collections.emptyList()
        );

        Mockito.when(categoryService.updateCategory(eq(id), any(CategoryRequest.class))).thenReturn(updatedResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/category/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Hatch2"))
                .andExpect(jsonPath("$.description").value("Um carro legalzão"));
    }

    @Test
    public void testDeleteCategory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/category/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testFindCategoryById() throws Exception {
        Mockito.when(categoryService.findCategoryById(id)).thenReturn(categoryResponse);

        String response = objectMapper.writeValueAsString(categoryResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/category/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
}



