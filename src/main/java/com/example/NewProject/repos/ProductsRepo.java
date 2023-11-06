package com.example.NewProject.repos;

import com.example.NewProject.entities.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepo extends JpaRepository<Products, Long> {

    @Query("SELECT p FROM Products p WHERE p.id =:prdId")
    Products findByProductId(@Param("prdId") long id);

}
