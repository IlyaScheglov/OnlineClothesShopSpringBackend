package com.example.NewProject.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "cost")
    private String cost;

    @Override
    public int compareTo(Products o) {
        return 0;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Categories category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "color_id")
    private Colors color;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    private List<ProductsOnStock> productsOnStock = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    private List<Images> images = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    private List<Reviews> reviews = new ArrayList<>();
}
