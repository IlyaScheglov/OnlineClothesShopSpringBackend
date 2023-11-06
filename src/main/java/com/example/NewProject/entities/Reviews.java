package com.example.NewProject.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "reviews")
public class Reviews implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "stars")
    private int stars;

    @Column(name = "review_text")
    private String reviewText;
}
