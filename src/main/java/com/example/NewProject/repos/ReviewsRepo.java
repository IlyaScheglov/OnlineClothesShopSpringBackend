package com.example.NewProject.repos;

import com.example.NewProject.entities.ProductsInOrder;
import com.example.NewProject.entities.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewsRepo extends JpaRepository<Reviews, Long> {

    @Query("SELECT r FROM Reviews r WHERE r.product.id = :prdId")
    List<Reviews> findByPrdId(@Param("prdId") long productId);

    @Query("SELECT r FROM Reviews r WHERE r.user.id = :usId AND r.product.id = :prdId")
    List<Reviews> findByUserAndProductIds(@Param("usId") long userId, @Param("prdId") long productId);

}
