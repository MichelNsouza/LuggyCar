package com.br.luggycar.api.validators;

import com.br.luggycar.api.validators.annotation.LegalAge;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class legalAge implements ConstraintValidator<LegalAge, Date> {
    @Override
    public void initialize(LegalAge constraintAnnotation) { }
    @Override
    public boolean isValid(Date dateBirth, ConstraintValidatorContext constraintValidatorContext) {

        LocalDate birthLocalDate = dateBirth.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate currentDate = LocalDate.now();

        int age = Period.between(birthLocalDate, currentDate).getYears();

        return age >= 18;
    }
}