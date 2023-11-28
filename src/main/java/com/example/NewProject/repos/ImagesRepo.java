package com.example.NewProject.repos;

import com.example.NewProject.entities.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagesRepo extends JpaRepository<Images, Long> {

    @Query("SELECT i FROM Images i WHERE i.product.id = :prdId")
    List<Images> findByPrdImId(@Param("prdId") long productId);
}
