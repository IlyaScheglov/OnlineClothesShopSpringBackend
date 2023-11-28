package com.example.NewProject.repos;

import com.example.NewProject.entities.Colors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorsRepo extends JpaRepository<Colors, Long> {

    @Query("SELECT c FROM Colors c WHERE c.id = :colId")
    Colors findByColorId(@Param("colId") long colorId);
}
