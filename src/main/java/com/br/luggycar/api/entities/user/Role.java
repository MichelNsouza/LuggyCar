package com.br.luggycar.api.entities.user;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "ROLE")
public class Role implements GrantedAuthority,Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,unique = true)
    private RoleName name;

    @Override
    public String getAuthority() {
        return this.name.toString();
    }
}
