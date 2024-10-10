package com.br.luggycar.api.repositories;


import com.br.luggycar.api.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClientRepository extends JpaRepository<Client, Long> {

}