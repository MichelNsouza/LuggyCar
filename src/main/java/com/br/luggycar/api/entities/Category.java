package com.br.luggycar.api.entities;



import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String image;

    @OneToMany(mappedBy = "category")
    //@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    //especificações de relacionamento que serão adicionadas posteriormente
    private List<Vehicle> vehicles;

    private LocalDateTime createdDate;




}
