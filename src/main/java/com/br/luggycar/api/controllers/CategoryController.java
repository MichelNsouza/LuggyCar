package com.br.luggycar.api.controllers;


import com.br.luggycar.api.dtos.response.CategoryResponse;
import com.br.luggycar.api.dtos.response.VehicleResponse;
import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.exceptions.ResourceExistsException;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.dtos.requests.CategoryRequest;
import com.br.luggycar.api.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest) {

        CategoryResponse savedCategory = categoryService.createCategory(categoryRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedCategory);

    }

    @GetMapping
    public List<CategoryResponse> readAllCategories() {
        return categoryService.readAllCategories();
    }


    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id, @RequestBody CategoryRequest categoryRequest) throws ResourceExistsException {
        CategoryResponse updatedCategoryResponse = categoryService.updateCategory(id, categoryRequest);
        return ResponseEntity
                .ok(updatedCategoryResponse);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable long id) throws ResourceExistsException{
        categoryService.deleteCategory(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findCategoryById(@PathVariable long id) {
        return ResponseEntity
                .ok(categoryService.findCategoryById(id));
    }

//    @GetMapping("/{name}")
//    public ResponseEntity<Category> findCategoryByName(@PathVariable String name) {
//        return ResponseEntity.ok(categoryService.findCategoryByName(name));
//    }

}
