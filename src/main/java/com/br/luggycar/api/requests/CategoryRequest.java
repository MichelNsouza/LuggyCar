package com.br.luggycar.api.requests;


import com.br.luggycar.api.entities.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {

    private String name;

    private String description;

    private String image;

    private Vehicle vehicle;

}