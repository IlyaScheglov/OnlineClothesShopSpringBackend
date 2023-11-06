package com.example.NewProject.repos;

import com.example.NewProject.entities.Statuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusesRepo extends JpaRepository<Statuses, Long> {

    @Query("SELECT s FROM Statuses s WHERE s.id = :stsId")
    Statuses findByStatusId(@Param("stsId") long statusId);
}
