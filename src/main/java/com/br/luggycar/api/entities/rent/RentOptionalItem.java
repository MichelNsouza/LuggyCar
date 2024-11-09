package com.br.luggycar.api.entities.rent;

import com.br.luggycar.api.entities.OptionalItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentOptionalItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rent_id")
    @JsonIgnore
    private Rent rent;
    @ManyToOne
    @JoinColumn(name = "optional_item_id")
    private OptionalItem optionalItem;

    private int quantity;

    public Double calculateTotal(){
        return this.optionalItem.getRentalValue() * this.quantity;
    }
}