package com.br.luggycar.api.services;


import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.entities.Rent;
import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.enums.rent.RentStatus;
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

        try {
            Category category = new Category();

            BeanUtils.copyProperties(categoryRequest, category);

            return categoryRepository.save(category);

        } catch (ResourceDatabaseException e) {
            throw new ResourceDatabaseException("Erro ao salvar a categoria no banco de dados", e);

        }

    }

    public List<Category> readAllCategories() {
        return categoryRepository.findAll();
    }

    public Category updateCategory(Long id, CategoryRequest categoryRequest)  {

        try {
            Optional<Category> category = categoryRepository.findById(id);

            List<Vehicle> vehicles = vehicleRepository.findByCategoryId(id);

            if (category.isPresent()) {

                Category categoryToUpdate = category.get();

                BeanUtils.copyProperties(categoryRequest, categoryToUpdate);

                return categoryRepository.save(categoryToUpdate);
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
                List<Rent> activeRents = rentRepository.findByVehicleIdAndStatus(vehicle.getId(), RentStatus.IN_PROGRESS);
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

    public Category findCategoryByName(String name) {
        return (Category) categoryRepository.findByName(name).get();
    }

    public Category findCategoryById(long id) {
        return categoryRepository.findById(id).get();
    }

}
