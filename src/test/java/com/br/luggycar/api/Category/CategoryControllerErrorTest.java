package com.br.luggycar.api.Category;

import com.br.luggycar.api.dtos.requests.CategoryRequest;
import com.br.luggycar.api.dtos.requests.DelayPenaltyRequest;
import com.br.luggycar.api.dtos.response.CategoryResponse;
import com.br.luggycar.api.exceptions.ResourceCategoryHasActiveVehicleException;
import com.br.luggycar.api.services.CategoryService;
import com.br.luggycar.api.exceptions.ResourceExistsException;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Test
    public void testCreateCategoryAlreadyExists() throws Exception {
        CategoryRequest categoryRequest = new CategoryRequest(
                "Hatch",
                "Um carro legalzão",
                List.of(new DelayPenaltyRequest(5, 20.0))
        );

        // Simula a exceção de categoria já existente
        Mockito.when(categoryService.createCategory(any(CategoryRequest.class)))
                .thenThrow(new ResourceExistsException("Categoria já existe!"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isBadRequest()) // Espera o código HTTP 400
                .andExpect(jsonPath("$.message").value("Categoria já existe!"));
    }




    @Test
    public void testUpdateCategoryNotFound() throws Exception {
        Long id = 1L;
        CategoryRequest categoryRequest = new CategoryRequest(
                "Hatch",
                "Um carro legalzão",
                List.of(new DelayPenaltyRequest(5, 20.0))
        );

        // Simulando a exceção de categoria não encontrada
        Mockito.when(categoryService.updateCategory(eq(id), any(CategoryRequest.class)))
                .thenThrow(new ResourceNotFoundException("Categoria não encontrada"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/category/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isNotFound()) // Esperado HTTP Status Code 404 Not Found
                .andExpect(jsonPath("$.message").value("Categoria não encontrada"));
    }

    @Test
    public void testDeleteCategoryNotFound() throws Exception {
        Long id = 1L;

        // Simulando a exceção de categoria não encontrada
        Mockito.doThrow(new ResourceNotFoundException("Categoria não encontrada"))
                .when(categoryService).deleteCategory(eq(id));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/category/{id}", id))
                .andExpect(status().isNotFound()) // Esperado HTTP Status Code 404 Not Found
                .andExpect(jsonPath("$.message").value("Categoria não encontrada"));
    }




    @Test
    public void testFindCategoryByIdNotFound() throws Exception {
        Long id = 1L;

        // Simulando a exceção de categoria não encontrada
        Mockito.when(categoryService.findCategoryById(eq(id)))
                .thenThrow(new ResourceNotFoundException("Categoria não encontrada"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/category/{id}", id))
                .andExpect(status().isNotFound()) // Esperado HTTP Status Code 404 Not Found
                .andExpect(jsonPath("$.message").value("Categoria não encontrada"));
    }

}




