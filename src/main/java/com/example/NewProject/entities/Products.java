package com.example.NewProject.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Products implements Serializable, Comparable<Products>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "small_description")
    private String smallDescription;

    @Column(name = "big_description")
    private String bigDescription;

    @Column(name = "color_id")
    private long colorId;

    @Column(name = "cost")
    private String cost;

    @Column(name = "category_id")
    private long categoryId;

    @Override
    public int compareTo(Products o) {
        return 0;
    }
}
