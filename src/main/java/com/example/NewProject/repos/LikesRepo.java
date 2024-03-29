package com.example.NewProject.repos;

import com.example.NewProject.entities.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikesRepo extends JpaRepository<Likes, Long> {

    @Query("SELECT l FROM Likes l WHERE l.user.id = :usId AND l.product.id = :prdId")
    List<Likes> checkLikedByUser(@Param("usId") long userId, @Param("prdId") long productId);

    @Query("SELECT l FROM Likes l WHERE l.user.id = :usId")
    List<Likes> findByUsId(@Param("usId") long userId);

    @Query("SELECT l FROM Likes l WHERE l.id = :lkId")
    Likes findByLikeId(@Param("lkId") long likeId);
}
