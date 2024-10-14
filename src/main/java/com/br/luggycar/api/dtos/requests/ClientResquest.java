package com.br.luggycar.api.dtos.requests;

import com.br.luggycar.api.enums.client.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

public record ClientResquest(
    String name,
    String lastName,
    String cpf,
    String email,
    Gender gender,
    Date dateBirth,
    String endereco
){}