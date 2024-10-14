package com.br.luggycar.api.services;


import com.br.luggycar.api.entities.Category;
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
    private CategoryRepository CategoryRepository;

    public Category createCategory(CategoryRequest categoryRequest) {

        Category category = new Category();
        BeanUtils.copyProperties(categoryRequest, category);

        return CategoryRepository.save(category);
    }

    public List<Category> readAllCategories() {
        return CategoryRepository.findAll();
    }

    public Category updateCategory(Long id, CategoryRequest categoryRequest) {

        Optional<Category> category = CategoryRepository.findById(id);

        if (category.isPresent()) {

            Category categoryToUpdate = category.get();

            BeanUtils.copyProperties(categoryRequest, categoryToUpdate);

            return CategoryRepository.save(categoryToUpdate);
        }
        return null;
    }

    public boolean deleteCategory(long id) {
        Optional<Category> category = CategoryRepository.findById(id);

        if (category.isPresent()) {
            CategoryRepository.delete(category.get());
            return true;
        } else {
            return false;
        }
    }

    public Category findCategoryByName(String name) {
        return (Category) CategoryRepository.findByName(name).get();
    }

    public Category findCategoryById(long id) {
        return CategoryRepository.findById(id).get();
    }

}
