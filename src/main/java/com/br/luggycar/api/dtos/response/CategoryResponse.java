package com.br.luggycar.api.dtos.response;

import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.entities.DelayPenalty;

import java.util.List;

public record CategoryResponse(
        Long id,
        String name,
        String description,
        List<DelayPenalty> delayPenalties
) {
    public CategoryResponse(Category category) {
        this(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getDelayPenalties()
        );
    }
}
