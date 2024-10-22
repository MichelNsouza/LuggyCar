package com.br.luggycar.api.services;


import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.exceptions.ResourceExistsException;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.repositories.CategoryRepository;
import com.br.luggycar.api.dtos.requests.CategoryRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

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

    public Category updateCategory(Long id, CategoryRequest categoryRequest) {

        Optional<Category> category = categoryRepository.findById(id);

        if (category.isPresent()) {

            Category categoryToUpdate = category.get();

            BeanUtils.copyProperties(categoryRequest, categoryToUpdate);

            return categoryRepository.save(categoryToUpdate);
        }
        return null;
    }

    public boolean deleteCategory(long id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isPresent()) {
            categoryRepository.delete(category.get());
            return true;
        } else {
            return false;
        }
    }

    public Category findCategoryByName(String name) {
        return (Category) categoryRepository.findByName(name).get();
    }

    public Category findCategoryById(long id) {
        return categoryRepository.findById(id).get();
    }

}
