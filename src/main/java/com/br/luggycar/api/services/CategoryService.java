package com.br.luggycar.api.services;


import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.entities.Rent;
import com.br.luggycar.api.entities.Vehicle;
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

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private RentRepository rentRepository;

    public Category createCategory(CategoryRequest categoryRequest) throws ResourceExistsException {

        Optional<Category> existingCategory = categoryRepository.findByName(categoryRequest.name());

        if (existingCategory.isPresent()) {
            throw new ResourceExistsException("Categoria já existe!");
        }

        Category category = new Category();
        BeanUtils.copyProperties(categoryRequest, category);

        return categoryRepository.save(category);
    }

    public List<Category> readAllCategories() {
        return categoryRepository.findAll();
    }

    public Category updateCategory(Long id, CategoryRequest categoryRequest)  {

        Optional<Category> category = categoryRepository.findById(id);

        List<Vehicle> vehicles = vehicleRepository.findByCategoryId(id);

        if (!vehicles.isEmpty()) {
            throw new IllegalArgumentException("Não é possível excluir a categoria enquanto houver veículos associados.");
        }

        if (category.isPresent()) {

            Category categoryToUpdate = category.get();

            BeanUtils.copyProperties(categoryRequest, categoryToUpdate);

            return categoryRepository.save(categoryToUpdate);
        }
        return null;
    }

    public boolean deleteCategory(long id) throws ResourceExistsException {
        Optional<Category> category = categoryRepository.findById(id);

        List<Vehicle> vehicles = vehicleRepository.findByCategoryId(id);

        for (Vehicle vehicle : vehicles) {
            List<Rent> activeRents = rentRepository.findByVehicleIdAndActive(vehicle.getId(), true);
            if (!activeRents.isEmpty()) {
                System.out.println("Locação ativa encontrada para o veículo: " + vehicle.getName());
                throw new ResourceExistsException("Não é possível excluir a categoria enquanto houver locações ativas para os veículos associados.");
            }
        }

        if (category.isPresent()) {
            categoryRepository.delete(category.get());
            return true;
        } else {
            throw new ResourceNotFoundException("Categoria não encontrada!");
        }
    }

    public Category findCategoryByName(String name) {
        return (Category) categoryRepository.findByName(name).get();
    }

    public Category findCategoryById(long id) {
        return categoryRepository.findById(id).get();
    }

}
