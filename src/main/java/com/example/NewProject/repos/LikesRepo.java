package com.example.NewProject.repos;

import com.example.NewProject.entities.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikesRepo extends JpaRepository<Likes, Long> {

    @Query("SELECT l FROM Likes l WHERE l.userId = :usId AND l.productId = :prdId")
    List<Likes> checkLikedByUser(@Param("usId") long userId, @Param("prdId") long productId);

    List<Likes> findByUserId(long userId);

    @Query("SELECT l FROM Likes l WHERE l.id = :lkId")
    Likes findByLikeId(@Param("lkId") long likeId);
}
