package com.example.NewProject.repos;

import com.example.NewProject.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepo extends JpaRepository<Orders, Long> {

    @Query("SELECT o FROM Orders o WHERE o.user.id = :usId")
    List<Orders> findByUsId(@Param("usId") long userId);

    @Query("SELECT o FROM Orders o WHERE o.id = :ordId")
    Orders findByOrderId(@Param("ordId") long orderId);
}
