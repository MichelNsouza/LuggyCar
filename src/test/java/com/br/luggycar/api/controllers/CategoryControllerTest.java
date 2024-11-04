package com.br.luggycar.api.controllers;

import com.br.luggycar.api.dtos.requests.CategoryRequest;
import com.br.luggycar.api.dtos.response.CategoryResponse;
import com.br.luggycar.api.entities.User;
import com.br.luggycar.api.enums.user.UserRole;
import com.br.luggycar.api.repositories.UserRepository;
import com.br.luggycar.api.services.CategoryService;
import com.br.luggycar.api.services.security.TokenService;
import com.br.luggycar.api.utils.AuthUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Collections;
import java.util.List;

import static java.time.LocalDate.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CategoryController.class)
@ActiveProfiles("test")
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private AuthUtil authUtil;

    private String token;

    private CategoryRequest categoryRequest;
    private CategoryResponse categoryResponse;

    @BeforeEach
    public void setUp() {

        User user = new User("ADMIN", "123456789", UserRole.ADMIN);

        Mockito.when(tokenService.generateToken(Mockito.any(User.class))).thenReturn("mocked-token");

        token = tokenService.generateToken(user);

        categoryRequest = new CategoryRequest("SUV", "Um carro legal", "link_para_imagem.jpg", now());
        categoryResponse = new CategoryResponse(1L, "SUV", "Um carro legal");

    }

    @Test
    public void testReadAllCategories() throws Exception {

        mockMvc.perform(get("/api/category")
                        .header("Authorization", "Bearer " + token) // Inclui o token no cabeçalho
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
//                .andExpect((ResultMatcher) jsonPath("$[0].id").value(1L))
//                .andExpect((ResultMatcher) jsonPath("$[0].name").value("SUV"))
//                .andExpect((ResultMatcher) jsonPath("$[0].description").value("Um carro legal"));
    }

}
