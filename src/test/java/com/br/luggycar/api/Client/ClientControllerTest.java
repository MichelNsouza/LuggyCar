package com.br.luggycar.api.Client;

import com.br.luggycar.api.dtos.requests.ClientRequest;
import com.br.luggycar.api.dtos.response.ClientResponse;
import com.br.luggycar.api.enums.client.Gender;
import com.br.luggycar.api.enums.client.PersonType;
import com.br.luggycar.api.enums.client.licenseCategory;
import com.br.luggycar.api.services.CategoryService;
import com.br.luggycar.api.services.ClientService;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDate.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    private Long responseId;
    private Long responseIdPJ;

    private Date birthDate;
    private Date birthDatePJ;
    private Date licenseValidity;
    private Date licenseValidityPJ;
    private ClientRequest clientRequest;
    private ClientRequest clientRequestPJ;
    private ClientResponse response;
    private ClientResponse responsePJ;


    @BeforeEach
    public void setUp() throws ParseException {

        responseId = 1L;
        responseIdPJ = 2L;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        birthDate = dateFormat.parse("1998-12-31");
        licenseValidity = dateFormat.parse("2025-12-12");

        birthDatePJ = dateFormat.parse("2001-11-18");
        licenseValidityPJ = dateFormat.parse("2026-12-12");

         clientRequest = new ClientRequest(
                PersonType.PF,
                "Michel Souza",
                "050.564.665-06",
                birthDate,
                Gender.MASCULINO,
                null,
                null,
                "michelphp@gmail.com",
                "42700-971",
                "Rua XPTO",
                "123456",
                licenseValidity,
                "B"
        );

         response = new ClientResponse(
                responseId,
                PersonType.PF,
                "Michel Souza",
                "050.564.665-06",
                null,
                null,
                "michelphp@gmail.com",
                "123456",
                licenseValidity,
                List.of(licenseCategory.B),
                Gender.MASCULINO,
                birthDate,
                "42700971",
                "Rua XPTO",
                now()
        );

        clientRequestPJ = new ClientRequest(
                PersonType.PJ,
                null,
                null,
                birthDatePJ,
                null,
                "99.957.150/0001-55",
                "Empresa XPTO",
                "empresa@xpto.com",
                "42700-971",
                "Avenida XPTO",
                "123456",
                licenseValidityPJ,
                "D"
        );

        responsePJ = new ClientResponse(
                responseIdPJ,
                PersonType.PJ,
                null,
                null,
                "99.957.150/0001-55",
                "Empresa XPTO",
                "empresa@xpto.com",
                "12345",
                licenseValidityPJ,
                List.of(licenseCategory.D),
                null,
                birthDatePJ,
                "42700971",
                "Avenida XPTO",
                now()
        );
    }

    @Test
    public void testCreateClientPF() throws Exception {

        Mockito.when(clientService.createClient(any(ClientRequest.class))).thenReturn(response);

        String expectedResponse = objectMapper.writeValueAsString(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
    }

    @Test
    public void testCreateClientPJ() throws Exception {
        Mockito.when(clientService.createClient(any(ClientRequest.class))).thenReturn(responsePJ);

        String expectedResponsePJ = objectMapper.writeValueAsString(responsePJ);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequestPJ)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(expectedResponsePJ));
    }

    @Test
    public void testUpdateClient() throws Exception {

        ClientRequest clientRequestUpdated = new ClientRequest(
                PersonType.PF,
                "Michel Atualizado",
                "050.564.665-06",
                birthDate,
                Gender.MASCULINO,
                null,
                null,
                "michelphp@gmail.com",
                "42700971",
                "Rua Atualizada",
                "123456",
                licenseValidity,
                "B"
        );


        ClientResponse updatedResponseUpdated = new ClientResponse(
                responseId,
                PersonType.PF,
                "Michel Atualizado",
                "050.564.665-06",
                null,
                null,
                "michelphp@gmail.com",
                "123456",
                licenseValidity,
                List.of(licenseCategory.B),
                Gender.MASCULINO,
                birthDate,
                "42700971",
                "Rua Atualizada",
                now()
        );

        Mockito.when(clientService.updateClient(eq(1L), any(ClientRequest.class))).thenReturn(updatedResponseUpdated);

        String expectedResponse = objectMapper.writeValueAsString(updatedResponseUpdated);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/client/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedResponse));

    }

    @Test
    public void testReadAllClient() throws Exception {
        Mockito.when(clientService.readAllClient()).thenReturn(List.of(response, responsePJ));

        String expectedResponse = objectMapper.writeValueAsString(List.of(response, responsePJ));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/client")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
    }

    @Test
    public void testDeleteClient() throws Exception {
        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/client/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testFindClientById() throws Exception {

        Mockito.when(clientService.findClientById(responseId)).thenReturn(response);

        String expectedResponse = objectMapper.writeValueAsString(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/client/{id}", responseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedResponse));

    }

}