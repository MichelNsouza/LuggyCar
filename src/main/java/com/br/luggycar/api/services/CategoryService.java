package com.br.luggycar.api.services;


import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository CategoryRepository;


    public Category SaveCategory(Category category){
        return CategoryRepository.save(category);
    }

    public Category findByName(String name){
        return (Category) CategoryRepository.findByName(name).get();
    }

    public Category findById(long id){
        return CategoryRepository.findById(id).get();
    }
    public List<Category> getAllCategories(){
        return CategoryRepository.findAll();
    }

//    public boolean updateCategory(Long id){
//
//
//    }

    public boolean deleteCategory(long id) {
        Optional<Category> category = CategoryRepository.findById(id);

        if (category.isPresent()) {
            CategoryRepository.delete(category.get());
            return true;
        } else {
            return false;
        }
    }


}
