package com.br.luggycar.api.Category;

import com.br.luggycar.api.dtos.requests.CategoryRequest;
import com.br.luggycar.api.dtos.response.CategoryResponse;
import com.br.luggycar.api.services.CategoryService;
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
import static java.time.LocalDate.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testCreateCategory() throws Exception {

        CategoryRequest categoryRequest = new CategoryRequest(
                "Hatch",
                "Um carro pequeno",
                "link_para_imagem.jpg",
                now()
        );

        CategoryResponse response = new CategoryResponse(
                1L,
                "Hatch",
                "Um carro pequeno"
        );

        Mockito.when(categoryService.createCategory(any(CategoryRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Hatch"))
                .andExpect(jsonPath("$.description").value("Um carro pequeno"));
    }

    @Test
    public void testReadAllCategories() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/category")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateCategory() throws Exception {
        Long id = 1L;

        CategoryRequest categoryRequest = new CategoryRequest(
                "Hatch",
                "Um carro legalzão",
                "link_para_imagem.jpg",
                now()
        );

        CategoryResponse response = new CategoryResponse(
                id,
                "Hatch2",
                "Um carro legalzão"
        );

        Mockito.when(categoryService.updateCategory(eq(id), any(CategoryRequest.class))).thenReturn(response);

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
        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/category/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testFindCategoryById() throws Exception {
        Long id = 1L;

        CategoryResponse response = new CategoryResponse(
                id,
                "Hatch",
                "Um carro pequeno"
        );

        Mockito.when(categoryService.findCategoryById(id)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/category/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Hatch"))
                .andExpect(jsonPath("$.description").value("Um carro pequeno"));
    }

//    @Test
//    public void testFindCategoryByName() throws Exception {
//        String name = "Hatch";
//        CategoryResponse response = new CategoryResponse(1L, name, "Um carro pequeno");
//
//        Mockito.when(categoryService.findCategoryByName(name)).thenReturn(response);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/category/name/{name}", name)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.name").value(name))
//                .andExpect(jsonPath("$.description").value("Um carro pequeno"));
//
//    }
    // -> FindCategoryyByName() no service está retornando um Category e no teste um CategoryResponse
}
