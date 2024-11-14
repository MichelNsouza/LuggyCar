package com.br.luggycar.api.services;


import com.br.luggycar.api.dtos.requests.DelayPenaltyRequest;
import com.br.luggycar.api.dtos.response.CategoryResponse;
import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.entities.DelayPenalty;
import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.exceptions.*;
import com.br.luggycar.api.repositories.CategoryRepository;
import com.br.luggycar.api.dtos.requests.CategoryRequest;
import com.br.luggycar.api.repositories.RentRepository;
import com.br.luggycar.api.repositories.VehicleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private VehicleRepository vehicleRepository;


    public CategoryResponse createCategory(CategoryRequest categoryRequest) throws ResourceDatabaseException, ResourceExistsException {

        Optional<Category> existingCategory = categoryRepository.findByName(categoryRequest.name());

        if (existingCategory.isPresent()) {
            throw new ResourceExistsException("Categoria já existe!");
        }

        Category category = new Category();
        BeanUtils.copyProperties(categoryRequest, category);


        category.setRegistration(LocalDate.now());


        if (categoryRequest.delayPenalties() != null && !categoryRequest.delayPenalties().isEmpty()) {
            List<DelayPenalty> penalties = new ArrayList<>();
            for (DelayPenaltyRequest penaltyRequest : categoryRequest.delayPenalties()) {
                DelayPenalty penalty = new DelayPenalty();
                penalty.setDays(penaltyRequest.days());
                penalty.setPercentage(penaltyRequest.percentage());
                penalty.setCategory(category);
                penalties.add(penalty);
            }
            category.setDelayPenalties(penalties);
        }


        Category savedCategory = categoryRepository.save(category);


        return new CategoryResponse(savedCategory);

    }


    public List<CategoryResponse> readAllCategories() throws ResourceDatabaseException {
        List<Category> categories = categoryRepository.findAll();
        return categories
                .stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());

    }

    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) throws ResourceDatabaseException {

        try {
                Category categoryUpdate = categoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

                BeanUtils.copyProperties(categoryRequest, categoryUpdate);

                categoryRepository.save(categoryUpdate);

                return new CategoryResponse(categoryUpdate);

        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteCategory(long id) throws ResourceDatabaseException {

        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Sem registros de categoria com o ID: " + id
                    ));

            List<Vehicle> vehicles = vehicleRepository.findByCategoryId(id);

            if (!vehicles.isEmpty()) {
                throw new ResourceCategoryHasActiveVehicleException("Não é possível remover a categoria com ID " + id + " porque ele possui veiculo associado.");
            }

            categoryRepository.delete(category);

        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ResourceCategoryHasActiveVehicleException e) {
            throw new RuntimeException(e);
        }

    }

    public Category findCategoryByName(String name) throws ResourceNotFoundException {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o nome: " + name));
    }

    public CategoryResponse findCategoryById(long id) throws ResourceNotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category não encontrado"));
        return new CategoryResponse(category);
    }

}
