package com.br.luggycar.api.Client;

import com.br.luggycar.api.dtos.requests.ClientRequest;
import com.br.luggycar.api.dtos.response.ClientResponse;
import com.br.luggycar.api.enums.client.Gender;
import com.br.luggycar.api.enums.client.PersonType;
import com.br.luggycar.api.enums.client.licenseCategory;
import com.br.luggycar.api.services.CategoryService;
import com.br.luggycar.api.services.ClientService;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateClient() throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = dateFormat.parse("1998-12-31");
        Date licenseValidity = dateFormat.parse("2025-12-12");

        ClientRequest clientRequest = new ClientRequest(
                PersonType.PF,
                "Michel Souza",
                "050.564.665-06",
                birthDate,
                Gender.MASCULINO,
                null,
                null,
                "michelphp@gmail.com",
                "41200100",
                "Rua XPTO",
                "123456",
                licenseValidity,
                "B"
        );

        ClientResponse response = new ClientResponse(
                1L,
                PersonType.PF,
                "Michel Souza",
                "050.564.665-06",
                null,
                null,
                "michelphp@gmail.com",
                "123456",
                licenseValidity,
                List.of(new licenseCategory[]{}),
                Gender.MASCULINO,
                birthDate,
                "41200100",
                "Rua XPTO",
                now()
                );

        Mockito.when(clientService.createClient(any(ClientRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequest)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.naturalPersonName").value("Michel Souza"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("050.564.665-06"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cnpj").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.companyName").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("michelphp@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("MASCULINO"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("michelphp@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drivers_license_number").value("123456"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.license_validity").value("2025-12-12"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.license_validity").value(licenseValidity))
                .andExpect(MockMvcResultMatchers.jsonPath("$.license_validity").value(licenseValidity))


    }

    @Test
    public void testReadAllClient() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/client")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}