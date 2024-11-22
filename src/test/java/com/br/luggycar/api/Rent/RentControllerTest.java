package com.br.luggycar.api.Rent;


import com.br.luggycar.api.dtos.requests.Optional.OptionalQuantityRequest;
import com.br.luggycar.api.dtos.requests.rent.RentRequestCreate;
import com.br.luggycar.api.dtos.response.rent.RentCreateResponse;
import com.br.luggycar.api.dtos.response.rent.RentResponse;
import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.entities.rent.Rent;
import com.br.luggycar.api.enums.client.Gender;
import com.br.luggycar.api.enums.client.PersonType;
import com.br.luggycar.api.enums.client.licenseCategory;
import com.br.luggycar.api.enums.rent.RentStatus;
import com.br.luggycar.api.enums.vehicle.VehicleColor;
import com.br.luggycar.api.enums.vehicle.VehicleManufacturer;
import com.br.luggycar.api.enums.vehicle.Vehicletransmission;
import com.br.luggycar.api.repositories.ClientRepository;
import com.br.luggycar.api.repositories.VehicleRepository;
import com.br.luggycar.api.services.RentService;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class RentControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private RentService rentService;

        @MockBean
        private ClientRepository clientRepository;

        @MockBean
        private VehicleRepository vehicleRepository;

        @Autowired
        private ObjectMapper objectMapper;

        private Client client;
        private Vehicle vehicle;
        private Rent rent;
        private RentRequestCreate rentRequestCreate;
        private RentRequestCreate rentRequestCreate2;
        private RentResponse rentResponse;

        @BeforeEach
        public void setUp() throws ParseException {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateBirth = dateFormat.parse("2024-11-11");
            Date licenseValidity = dateFormat.parse("2025-11-11");

            client = new Client();
            client.setId(1L);
            client.setPersonType(PersonType.PF);
            client.setDateBirth(dateBirth);
            client.setCep("41200100");
            client.setEndereco("Rua Portão");
            client.setRegistration(LocalDate.now());
            client.setEmail("railan_fab@gmail.com");
            client.setCpf("050.564.665-06");
            client.setGender(Gender.MASCULINO);
            client.setNaturalPersonName("Railan Ibraim");
            client.setDrivers_license_number("123456");
            client.setDrivers_license_validity(licenseValidity);
            client.setDrivers_license_category(Arrays.asList(licenseCategory.B));

            vehicle = new Vehicle();
            vehicle.setId(1L);
            vehicle.setName("Gol");
            vehicle.setManufacturer(VehicleManufacturer.VOLKSWAGEN);
            vehicle.setVersion("1.0");
            vehicle.setPlate("ABC-1234");
            vehicle.setColor(VehicleColor.RED);
            vehicle.setTransmission(Vehicletransmission.MANUAL);
            vehicle.setCurrentKm(15000.0);
            vehicle.setPassangerCapacity("4");
            vehicle.setTrunkCapacity("200.0");
            vehicle.setDailyRate(100.0);
            vehicle.setRegistrationDate(LocalDate.now());

            rent = new Rent();
            rent.setId(1L);
            rent.setStatus(RentStatus.IN_PROGRESS);
            rent.setUser("ADMIN");
            rent.setClient(client);
            rent.setVehicle(vehicle);
            rent.setTotalDays(10);
            rent.setSecurityDeposit(200.00);
            rent.setStartDate(LocalDate.parse("2021-11-12"));
            rent.setDailyRate(100.0);
            rent.setTotalValue(1000.0);
            rent.setKmInitial(15000.0);
            rent.setCreate_at(LocalDate.now());
            rent.setRentOptionalItems(new ArrayList<>());

             rentRequestCreate = new RentRequestCreate(
                    1L,
                    1L,
                    10,
                    200.00,
                    null, // Criando uma locação sem optional
                    LocalDate.parse("2021-11-12"),
                    LocalDate.now()
            );

            rentRequestCreate2 = new RentRequestCreate(
                    1L,
                    1L,
                    10,
                    200.00,
                    List.of(new OptionalQuantityRequest(1L, 10)), // Criando uma locação com optional
                    LocalDate.parse("2021-11-12"),
                    LocalDate.now()
            );

            rentResponse = new RentResponse(
                    1L,
                    RentStatus.IN_PROGRESS,
                    "admin",
                    1L,
                    2L,
                    500.0,
                    LocalDate.now(),
                    5,
                    LocalDate.now().plusDays(5),
                    null,
                    null,
                    100.0,
                    20.0,
                    600.0,
                    100.0,
                    null, // kmFinal
                    LocalDate.now(),
                    null // update_at
            );

        }

        @Test
        public void testCreateRentNoOptional() throws Exception {

                RentCreateResponse rentCreateResponse = new RentCreateResponse(rent);

                Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
                Mockito.when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

                Mockito.when(rentService.createRent(Mockito.any(RentRequestCreate.class))).thenReturn(rentCreateResponse);

                String response = objectMapper.writeValueAsString(rentCreateResponse);

                mockMvc.perform(MockMvcRequestBuilders.post("/api/rent")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(rentRequestCreate)))
                            .andExpect(status().isCreated())
                            .andExpect(MockMvcResultMatchers.content().json(response));
        }

        @Test
        public void testReadAllRent() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/rent")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

//        @Test
//        public void testUpdateRent() throws Exception {
//            Long id = 1L;
//
//            RentRequestUpdate rentRequestUpdate = new RentRequestUpdate(
//                    RentStatus.IN_PROGRESS,
//                    "ADMIN",
//                    client,
//                    vehicle,
//                    5,
//                    100.0,
//                    null,
//                    null,
//                    100.0,
//                    15000.0,
//                    15800.0,
//                    null,
//                    null,
//                    LocalDate.now(),
//                    LocalDate.now()
//            );
//
//            // Locação atualizada
//            Rent updatedRent = new Rent();
//            updatedRent.setId(1L);
//            updatedRent.setStatus(RentStatus.IN_PROGRESS);
//            updatedRent.setUser("ADMIN");
//            updatedRent.setClient(client);
//            updatedRent.setVehicle(vehicle);
//            updatedRent.setTotalDays(5); // Valor atualizado
//            updatedRent.setSecurityDeposit(100.00); // Valor atualizado
//            updatedRent.setStartDate(LocalDate.parse("2021-11-13")); // Data atualizada
//            updatedRent.setDailyRate(100.0);
//            updatedRent.setTotalValue(500.0);
//            updatedRent.setCreate_at(LocalDate.now());
//
//            RentCreateResponse updatedResponse = new RentCreateResponse(updatedRent);
//
//            // Mocking do serviço
//            Mockito.when(rentService.updateRent(Mockito.eq(id), Mockito.any(RentRequestUpdate.class)))
//                    .thenReturn(updatedResponse);
//
//            // Execução do teste
//            mockMvc.perform(MockMvcRequestBuilders.put("/api/rent/{id}", id)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(rentRequestUpdate)))
//                    .andExpect(status().isOk())
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("IN_PROGRESS"))
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.user").value("ADMIN"))
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(1))
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.vehicleId").value(1))
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.deposit").value(100.00))
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.totalDays").value(5));
//        }


    @Test
    public void testDeleteRent() throws Exception {
        Long id = 1L;

        // Simulando que a exclusão não pode ser feita (veículo com aluguel ativo)
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/vehicle/{id}", id))
                .andExpect(status().isNotFound());
    }


    @Test
        public void findRentById() throws Exception {
            Long id = 1L;

            Mockito.when(rentService.findRentById(id)).thenReturn(Optional.of(rentResponse));

            String expectedResponse = objectMapper.writeValueAsString(rentResponse);


            mockMvc.perform(MockMvcRequestBuilders.get("/api/rent/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
        }

}

