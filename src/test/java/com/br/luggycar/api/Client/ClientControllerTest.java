package com.br.luggycar.api.Client;

import com.br.luggycar.api.services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static java.time.LocalDate.now;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    public void testCreateClient() throws Exception {
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date birthDate = dateFormat.parse("1998-12-31");
//
//        ClientRequest clientRequest = new ClientRequest(
//                PersonType.PF,
//                "Michel Souza",
//                "050.564.665-06",
//                birthDate,
//                Gender.MASCULINO,
//                null,
//                null,
//                "michelphp@gmail.com",
//                "41200100",
//                "Rua XPTO"
//        );
//
//        ClientResponse response = new ClientResponse(
//                1L,
//                PersonType.PF,
//                "Michel Souza",
//                "050.564.665-06",
//                null,
//                null,
//                "michelphp@gmail.com",
//                Gender.MASCULINO,
//                birthDate,
//                "41200100",
//                "Rua XPTO",
//                now()
//                );
//
//        Mockito.when(categoryService.createCategory(any(CategoryRequest.class))).thenReturn(response);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/client")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(clientRequest)))
//                .andExpect(status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Hatch"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Um carro pequeno"));
//    }

    @Test
    public void testReadAllClient() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/client")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}