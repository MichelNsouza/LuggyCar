package com.br.luggycar.api.services;


import com.br.luggycar.api.dtos.response.CategoryResponse;
import com.br.luggycar.api.dtos.response.ClientResponse;
import com.br.luggycar.api.dtos.response.VehicleResponse;
import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.entities.Rent;
import com.br.luggycar.api.entities.Vehicle;
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

        Optional<Category> existingCategory = categoryRepository.findByName(categoryRequest.name());

        if (existingCategory.isPresent()) {
            throw new ResourceExistsException("Categoria já existe!");
        }

        try {
            Category category = new Category();

            BeanUtils.copyProperties(categoryRequest, category);

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
            Optional<Category> category = categoryRepository.findById(id);

            List<Vehicle> vehicles = vehicleRepository.findByCategoryId(id);

            if (category.isPresent()) {

                Category categoryToUpdate = category.get();

                BeanUtils.copyProperties(categoryRequest, categoryToUpdate);

                categoryRepository.save(categoryToUpdate);

                return new CategoryResponse(categoryToUpdate);

            } else {
                throw new ResourceExistsException("Categoria não encontrada!");
            }

        } catch (ResourceDatabaseException e) {
            throw new ResourceDatabaseException("Erro ao atualizar a categoria no banco de dados", e);

        }

    }

    public boolean deleteCategory(long id) {

        try {
            Optional<Category> category = categoryRepository.findById(id);

            List<Vehicle> vehicles = vehicleRepository.findByCategoryId(id);

            for (Vehicle vehicle : vehicles) {
                List<Rent> activeRents = rentRepository.findByVehicleIdAndActive(vehicle.getId(), true);
                if (!activeRents.isEmpty()) {
                    throw new ResourceExistsException("Não é possível excluir a categoria pois existe locação ativa.");
                }
            }

            if (category.isPresent()) {
                categoryRepository.delete(category.get());
                return true;
            } else {
                throw new ResourceNotFoundException("Categoria não encontrada!");
            }

        } catch (ResourceDatabaseException e) {
            throw new ResourceDatabaseException("Erro ao deletar a categoria no banco de dados", e);
        }

    }

//    public Category findCategoryByName(String name) {
////        return (Category) categoryRepository.findByName(name).get();
//        return categoryRepository.findByName(name)
//                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o nome: " + name));
//    }

    public CategoryResponse findCategoryById(long id) {
//        return categoryRepository.findById(id).get();
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category não encontrado"));
        return new CategoryResponse(category);
    }

}
