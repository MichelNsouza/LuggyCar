package com.br.luggycar.api.controllers;


import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.exceptions.ResourceExistsException;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.dtos.requests.CategoryRequest;
import com.br.luggycar.api.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest categoryRequest) throws ResourceExistsException {
        return ResponseEntity.ok(categoryService.createCategory(categoryRequest));
    }

    @GetMapping
    public List<Category> readAllCategories() {
        return categoryService.readAllCategories();
    }


    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest)
            throws ResourceExistsException {
        return ResponseEntity.ok().body(categoryService.updateCategory(id, categoryRequest));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteCategory(@PathVariable long id) throws ResourceExistsException{
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Category> findCategoryById(@PathVariable long id) {
        return ResponseEntity.ok(categoryService.findCategoryById(id));
    }

    @GetMapping("/{name}")
    public ResponseEntity<Category> findCategoryByName(@PathVariable String name) {
        return ResponseEntity.ok(categoryService.findCategoryByName(name));
    }

}
