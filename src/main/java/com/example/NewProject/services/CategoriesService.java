package com.example.NewProject.services;

import com.example.NewProject.entities.Categories;
import com.example.NewProject.repos.CategoriesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriesService {

    private final CategoriesRepo categoriesRepo;

    public List<Categories> findAllCategories(){

        return categoriesRepo.findAll();
    }

    public Categories findCategoryById(long categoryId){
        return categoriesRepo.findByCategoryId(categoryId);
    }
}
