package com.example.NewProject.repos;

import com.example.NewProject.entities.Colors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorsRepo extends JpaRepository<Colors, Long> {
}
