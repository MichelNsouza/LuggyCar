package com.br.luggycar.api.Client;

import com.br.luggycar.api.dtos.requests.ClientRequest;
import com.br.luggycar.api.dtos.response.ClientResponse;
import com.br.luggycar.api.enums.client.Gender;
import com.br.luggycar.api.enums.client.PersonType;
import com.br.luggycar.api.enums.client.licenseCategory;
import com.br.luggycar.api.exceptions.ResourceClientHasActiveRentalsException;
import com.br.luggycar.api.exceptions.ResourceExistsException;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.time.LocalDate.now;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ClientControllerErrorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    private Date birthDate;
    private Date birthDatePJ;
    private Date licensePast;
    private Date pastBirthDate;
    private Date licenseValidity;
    private Date licenseValidityPJ;

    private ClientRequest clientRequest;
    private ClientRequest clientRequestPJ;
    private ClientRequest clientRequestUpdate;
    private ClientRequest clientRequestValidityInvalid;
    private ClientRequest clientRequestBirthDateInvalid;
    private ClientRequest clientRequestInvalidEmail;
    private ClientRequest clientRequestInvalidCpf;
    private ClientRequest clientRequestInvalidCnpj;

    @BeforeEach
    public void setUp() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        birthDate = dateFormat.parse("1998-12-31");
        licenseValidity = dateFormat.parse("2025-12-12");

        birthDatePJ = dateFormat.parse("2001-11-18");
        licenseValidityPJ = dateFormat.parse("2026-12-12");
        licensePast = dateFormat.parse("2020-03-03");
        pastBirthDate = dateFormat.parse("2020-01-01");

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

         clientRequestUpdate = new ClientRequest(
                PersonType.PF,
                "Michel Souza",
                "123.456.789-10",
                birthDate,
                Gender.MASCULINO,
                null,
                null,
                "euamophp@email.com",
                "12345-678",
                "Rua PHP",
                "654321",
                licenseValidity,
                "C"
        );

         clientRequestValidityInvalid = new ClientRequest(
                PersonType.PF,
                "Michel Souza",
                "050.564.665-06",
                birthDate,
                Gender.MASCULINO,
                null,
                null,
                "michelphp@gmail.com",
                "42700-971",
                "Rua dos amantes de PHP",
                "123456",
                 licensePast,
                "B"
        );

        clientRequestBirthDateInvalid = new ClientRequest(
                PersonType.PF,
                "Michel Souza",
                "050.564.665-06",
                pastBirthDate,
                Gender.MASCULINO,
                null,
                null,
                "michelphp@gmail.com",
                "42700-971",
                "Rua dos amantes de PHP",
                "123456",
                licenseValidity,
                "B"
        );


        clientRequestInvalidEmail = new ClientRequest(
                PersonType.PF,
                "Thales Almeida",
                "050.564.665-06",
                birthDate,
                Gender.MASCULINO,
                null,
                null,
                "thalecogmail.com",
                "42700-971",
                "Rua da Creatina",
                "123456",
                licenseValidity,
                "B"
        );

        clientRequestInvalidCpf= new ClientRequest(
                PersonType.PF,
                "Railan Ibraim",
                "12312312345",
                birthDate,
                Gender.MASCULINO,
                null,
                null,
                "railanmilitar@gmail.com",
                "42700-971",
                "Rua Aeronautica",
                "123456",
                licenseValidity,
                "D"
        );

        clientRequestInvalidCnpj = new ClientRequest(
                PersonType.PJ,
                null,
                null,
                birthDatePJ,
                null,
                "99.957.150/1212-54",
                "Empresa Ltda",
                "empresa@xpto.com",
                "42700-971",
                "Avenida da empresa",
                "123456",
                licenseValidityPJ,
                "B"
        );



    }

    @Test
    public void testCreateClientWithDuplicateCpf() throws Exception {

        Mockito.when(clientService.createClient(any(ClientRequest.class)))
                .thenThrow(new ResourceExistsException("já existe um cliente com esse documento."));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("já existe um cliente com esse documento."));
    }

    @Test
    public void testCreateClientDuplicateCnpj() throws Exception {

        Mockito.when(clientService.createClient(any(ClientRequest.class)))
                .thenThrow(new ResourceExistsException("já existe um cliente com esse documento."));


        mockMvc.perform(MockMvcRequestBuilders.post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequestPJ)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("já existe um cliente com esse documento."));
    }

    @Test
    public void testCreateClientDuplicateCep() throws Exception {
        Mockito.when(clientService.createClient(any(ClientRequest.class)))
                .thenThrow(new ResourceExistsException("Já existe um cliente com esse CEP."));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Já existe um cliente com esse CEP."));
    }

    @Test
    public void testUpdateClientNotFound() throws Exception {
        Long Id = 99L;

        Mockito.when(clientService.updateClient(Mockito.eq(Id), any(ClientRequest.class)))
                .thenThrow(new ResourceNotFoundException("Sem registros de cliente com o ID: " + Id));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/client/{id}", Id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequestUpdate)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Sem registros de cliente com o ID: " + Id));
    }

    @Test
    public void testDeleteClientWithActiveRentals() throws Exception {
        Long clientIdWithActiveRentals = 1L;

        Mockito.doThrow(new ResourceClientHasActiveRentalsException(
                        "Não é possível remover o cliente com ID "
                                + clientIdWithActiveRentals + " porque ele possui aluguéis ativos."))
                .when(clientService).deleteClient(clientIdWithActiveRentals);


        mockMvc.perform(MockMvcRequestBuilders.delete("/api/client/{id}", clientIdWithActiveRentals))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(
                        "Não é possível remover o cliente com ID "
                                + clientIdWithActiveRentals + " porque ele possui aluguéis ativos."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testCreateClientInvalidCpf() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequestInvalidCpf)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("CPF inválido"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testCreateClientInvalidCnpj() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequestInvalidCnpj)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cnpj").value("CNPJ inválido"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testCreateClientInvalidEmail() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequestInvalidEmail)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email")
                        .value("Email inválido"));
    }

//    @Test
//    public void testCreateClientLicenseValidityInvalid() throws Exception { // Erro de validação de data
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/client")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(clientRequestValidityInvalid)))
//                .andExpect(status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.drivers_license_validity")
//                        .value("A validade deve ser no futuro"));
//    }

    @Test
    public void testCreateClientDateBirthInvalid() throws Exception { // Erro de validação de data

        mockMvc.perform(MockMvcRequestBuilders.post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequestBirthDateInvalid)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateBirth")
                        .value("O cliente deve ser maior de idade!"));
    }

}
