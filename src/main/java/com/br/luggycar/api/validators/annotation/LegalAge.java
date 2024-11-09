package com.br.luggycar.api.validators.annotation;

import com.br.luggycar.api.validators.legalAge;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = legalAge.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LegalAge {
    String message() default "O cliente deve ser maior de idade!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}