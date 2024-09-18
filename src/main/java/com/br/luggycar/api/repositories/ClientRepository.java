package com.br.luggycar.api.repositories;


import ch.qos.logback.core.net.server.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface ClientRepository extends JpaRepository<Client, Long> {
}