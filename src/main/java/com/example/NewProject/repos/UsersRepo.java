package com.example.NewProject.repos;

import com.example.NewProject.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepo extends JpaRepository<Users, Long> {

    Users findByUsername(String username);
}
