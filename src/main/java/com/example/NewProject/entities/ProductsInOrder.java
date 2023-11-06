package com.example.NewProject.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "products_in_order")
public class ProductsInOrder implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "product_in_stock_id")
    private long productOnStockId;

    @Column(name = "count")
    private int count;
}
