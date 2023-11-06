package com.example.NewProject.repos;

import com.example.NewProject.entities.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagesRepo extends JpaRepository<Images, Long> {

    List<Images> findByProductId(long productId);
}
