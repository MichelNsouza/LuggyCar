package com.br.luggycar.api.services;


import com.br.luggycar.api.dtos.response.CategoryResponse;
import com.br.luggycar.api.dtos.response.ClientResponse;
import com.br.luggycar.api.dtos.response.VehicleResponse;
import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.entities.Rent;
import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.enums.rent.RentStatus;
import com.br.luggycar.api.exceptions.ResourceClientHasActiveRentalsException;
import com.br.luggycar.api.exceptions.ResourceDatabaseException;
import com.br.luggycar.api.exceptions.ResourceExistsException;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.repositories.CategoryRepository;
import com.br.luggycar.api.dtos.requests.CategoryRequest;
import com.br.luggycar.api.repositories.RentRepository;
import com.br.luggycar.api.repositories.VehicleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private RentRepository rentRepository;

    public CategoryResponse createCategory(CategoryRequest categoryRequest) {

//        Optional<Category> existingCategory = categoryRepository.findByName(categoryRequest.name());
//
//        if (existingCategory.isPresent()) {
//            throw new ResourceExistsException("Categoria já existe!");
//        }

        try {
            Category category = new Category();

            BeanUtils.copyProperties(categoryRequest, category);

            category.setRegistration(LocalDate.now());

            Category savedCategory = categoryRepository.save(category);

            return new CategoryResponse(savedCategory);

        } catch (ResourceDatabaseException e) {
            throw new ResourceDatabaseException("Erro ao salvar a categoria no banco de dados", e);

        }

    }

    public List<CategoryResponse> readAllCategories() {
        try {
            List<Category> categories = categoryRepository.findAll();
            return categories
                    .stream()
                    .map(CategoryResponse::new)
                    .collect(Collectors.toList());

        } catch (ResourceDatabaseException e) {
            throw new ResourceDatabaseException("Erro ao Buscar categorias no banco de dados", e);
        }

    }

    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest)  {

        try {
                Category categoryUpdate = categoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

                BeanUtils.copyProperties(categoryRequest, categoryUpdate);

                categoryRepository.save(categoryUpdate);

                return new CategoryResponse(categoryUpdate);

        } catch (ResourceDatabaseException e) {
            throw new ResourceDatabaseException("Erro ao atualizar a categoria no banco de dados", e);

        }

    }

    public void deleteCategory(long id) {

        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Sem registros de categoria com o ID: " + id
                    ));

            if (this.hasActiveRentals(id)) {
                throw new ResourceClientHasActiveRentalsException("Não é possível remover a categoria com ID " + id + " porque ele possui aluguéis ativos.");
            }

            categoryRepository.delete(category);

        } catch (ResourceDatabaseException e) {
            throw new ResourceDatabaseException("Erro ao deletar a categoria no banco de dados", e);
        }

    }

//    public Category findCategoryByName(String name) {
////        return (Category) categoryRepository.findByName(name).get();
//        return categoryRepository.findByName(name)
//                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o nome: " + name));
//    }

    public boolean hasActiveRentals(Long clientId) {
        return rentRepository.existsActiveRentByClientId(clientId, RentStatus.ATIVO);
    }

    public CategoryResponse findCategoryById(long id) {
//        return categoryRepository.findById(id).get();
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category não encontrado"));
        return new CategoryResponse(category);
    }

}
