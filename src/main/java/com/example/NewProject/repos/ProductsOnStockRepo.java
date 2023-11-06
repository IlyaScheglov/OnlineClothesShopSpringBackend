package com.example.NewProject.repos;

import com.example.NewProject.entities.ProductsOnStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsOnStockRepo extends JpaRepository<ProductsOnStock, Long> {

    @Query("SELECT p FROM ProductsOnStock p WHERE p.id = :prdOnStockId")
    ProductsOnStock findByProductOnStockId(@Param("prdOnStockId") long id);

    @Query("SELECT p.productId FROM ProductsOnStock p WHERE p.size = :productSize")
    List<Long> findProductIdsBySize(@Param("productSize") int productSize);

    List<ProductsOnStock> findByProductId(long productId);

    @Query("SELECT p FROM ProductsOnStock p WHERE p.productId = :prdId AND p.size = :prdSize")
    ProductsOnStock findByProductIdAndSize(@Param("prdId") long productId, @Param("prdSize") int size);

    @Query("SELECT p FROM ProductsOnStock p WHERE p.id = :prdId")
    List<ProductsOnStock> findByItsId(@Param("prdId") long productOnStockId);
}
