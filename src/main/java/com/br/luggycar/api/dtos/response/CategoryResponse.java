package com.br.luggycar.api.dtos.response;

import com.br.luggycar.api.entities.Category;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

public record CategoryResponse(
        Long id,
        String name,
        String description
//        String image
) {
    public CategoryResponse(Category category) {
        this(
                category.getId(),
                category.getName(),
                category.getDescription()
//                category.getImage()
        );
    }
}
