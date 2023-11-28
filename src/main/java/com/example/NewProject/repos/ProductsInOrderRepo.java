package com.example.NewProject.repos;

import com.example.NewProject.entities.ProductsInOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsInOrderRepo extends JpaRepository<ProductsInOrder, Long> {

    @Query("SELECT pio FROM ProductsInOrder pio WHERE pio.order.id = :ordId")
    List<ProductsInOrder> findByOrdId(@Param("ordId") long orderId);
}
