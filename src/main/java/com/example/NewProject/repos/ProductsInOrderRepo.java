package com.example.NewProject.repos;

import com.example.NewProject.entities.ProductsInOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsInOrderRepo extends JpaRepository<ProductsInOrder, Long> {

    List<ProductsInOrder> findByOrderId(long orderId);
}
