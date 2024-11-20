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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String PREFIXO_CACHE_REDIS = "category:";


    public CategoryResponse createCategory(CategoryRequest categoryRequest) {

        Optional<Category> existingCategory = categoryRepository.findByName(categoryRequest.name());

        if (existingCategory.isPresent()) {
            throw new ResourceExistsException("Categoria já existe!");
        }

        try {

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

            redisTemplate.delete(PREFIXO_CACHE_REDIS + "all_categories");

            return new CategoryResponse(savedCategory);

        } catch (ResourceDatabaseException e) {
            throw new ResourceDatabaseException("Erro ao salvar a categoria no banco de dados", e);
        }
    }


    public List<CategoryResponse> readAllCategories() {
        String cacheKey = PREFIXO_CACHE_REDIS + "all_categories";

        try {
            List<Category> cachedCategories = (List<Category>) redisTemplate.opsForValue().get(cacheKey);
            if (cachedCategories != null) {
                return cachedCategories
                  .stream()
                  .map(CategoryResponse::new)
                  .collect(Collectors.toList());
            }

            List<Category> categories = categoryRepository.findAll();
            redisTemplate.opsForValue().set(cacheKey, categories, 3, TimeUnit.DAYS);

            return categories.stream().map(CategoryResponse::new).collect(Collectors.toList());

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

                redisTemplate.delete(PREFIXO_CACHE_REDIS + "category_id:" + id);
                redisTemplate.delete(PREFIXO_CACHE_REDIS + "all_categories");

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

            List<Vehicle> vehicles = vehicleRepository.findByCategoryId(id);

            if (!vehicles.isEmpty()) {
                throw new ResourceCategoryHasActiveVehicleException("Não é possível remover a categoria com ID " + id + " porque ele possui veiculo associado.");
            }

            categoryRepository.delete(category);

            redisTemplate.delete(PREFIXO_CACHE_REDIS + "category_id:" + id);
            redisTemplate.delete(PREFIXO_CACHE_REDIS + "all_categories");

        } catch (ResourceDatabaseException e) {
            throw new ResourceDatabaseException("Erro ao deletar a categoria no banco de dados", e);
        }

    }

    public Category findCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o nome: " + name));
    }

    public CategoryResponse findCategoryById(long id) {
        String cacheKey = PREFIXO_CACHE_REDIS + "category_id:" + id;

        try {
            Category cachedCategory = (Category) redisTemplate.opsForValue().get(cacheKey);
            if (cachedCategory != null) {
                return new CategoryResponse(cachedCategory);
            }


            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Category não encontrado"));
            return new CategoryResponse(category);

        } catch (ResourceDatabaseException e) {
            throw new ResourceDatabaseException("Erro ao buscar categoria no banco de dados", e);
        }
    }

}
