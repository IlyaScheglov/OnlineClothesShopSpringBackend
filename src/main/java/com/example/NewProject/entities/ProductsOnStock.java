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
@Table(name = "products_on_stock")
public class ProductsOnStock implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "size")
    private int size;

    @Column(name = "count")
    private int count;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Products product;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "productsOnStock")
    private List<Basket> baskets = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "productsOnStock")
    private List<ProductsInOrder> productsInOrder = new ArrayList<>();
}
