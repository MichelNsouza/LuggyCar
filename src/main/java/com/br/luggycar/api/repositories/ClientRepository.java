package com.br.luggycar.api.repositories;


import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.entities.Rent;
import com.br.luggycar.api.enums.rent.RentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByCpf(String cpf);
    Optional<Client> findByCnpj(String cnpj);

}