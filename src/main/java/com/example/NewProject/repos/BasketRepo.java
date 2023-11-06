package com.example.NewProject.repos;

import com.example.NewProject.entities.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketRepo extends JpaRepository<Basket, Long> {

    List<Basket> findByUserId(long userId);

    @Query("SELECT b FROM Basket b WHERE b.id = :bskId")
    Basket findByBasketId(@Param("bskId") long basketId);
}
