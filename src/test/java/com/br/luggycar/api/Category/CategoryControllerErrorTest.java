package com.br.luggycar.api.Category;

import com.br.luggycar.api.dtos.requests.CategoryRequest;
import com.br.luggycar.api.dtos.requests.DelayPenaltyRequest;
import com.br.luggycar.api.dtos.response.CategoryResponse;
import com.br.luggycar.api.exceptions.ResourceCategoryHasActiveVehicleException;
import com.br.luggycar.api.services.CategoryService;
import com.br.luggycar.api.exceptions.ResourceExistsException;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerErrorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryRequest categoryRequest;
    private Long id;

    @BeforeEach
    public void setUp() {
        id = 1L;
        categoryRequest = new CategoryRequest(
                "Hatch",
                "Um carro legalzão",
                List.of(new DelayPenaltyRequest(5, 20.0))
        );
    }

    @Test
    public void testCreateCategoryAlreadyExists() throws Exception {
        Mockito.when(categoryService.createCategory(any(CategoryRequest.class)))
                .thenThrow(new ResourceExistsException("Categoria já existe!"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Categoria já existe!"));
    }

    @Test
    public void testUpdateCategoryNotFound() throws Exception {
        Mockito.when(categoryService.updateCategory(eq(id), any(CategoryRequest.class)))
                .thenThrow(new ResourceNotFoundException("Categoria não encontrada"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/category/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Categoria não encontrada"));
    }

    @Test
    public void testDeleteCategoryNotFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Categoria não encontrada"))
                .when(categoryService).deleteCategory(eq(id));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/category/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Categoria não encontrada"));
    }

    @Test
    public void testFindCategoryByIdNotFound() throws Exception {
        Mockito.when(categoryService.findCategoryById(eq(id)))
                .thenThrow(new ResourceNotFoundException("Categoria não encontrada"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/category/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Categoria não encontrada"));
    }
}





