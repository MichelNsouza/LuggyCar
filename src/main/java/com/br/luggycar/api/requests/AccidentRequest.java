package com.br.luggycar.api.requests;

import com.br.luggycar.api.enums.accident.Severity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.Date;

public class AccidentRequest {


    private Severity severity;
    private String description;
    private Date registrationDate;
}
