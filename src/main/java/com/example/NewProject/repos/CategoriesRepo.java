package com.example.NewProject.repos;

import com.example.NewProject.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepo extends JpaRepository<Categories, Long> {

    @Query("SELECT c FROM Categories c WHERE c.id = :catId")
    Categories findByCategoryId(@Param("catId") long categoryId);
}
