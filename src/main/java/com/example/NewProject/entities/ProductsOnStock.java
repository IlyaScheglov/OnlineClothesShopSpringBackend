package com.example.NewProject.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "products_on_stock")
public class ProductsOnStock implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "size")
    private int size;

    @Column(name = "count")
    private int count;
}
