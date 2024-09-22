package com.br.luggycar.api.repositories;

import com.br.luggycar.api.entities.user.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<SystemUser, UUID> {
    Optional<SystemUser> findByName(String name);
}
