package com.example.NewProject.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Orders implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "address")
    private String address;

    @Column(name = "status_id")
    private long statusId;

    @Column(name = "cost")
    private String cost;

    @Column(name = "final_address")
    private String finalAddress;

    @Column(name = "fio")
    private String fio;

    @Column(name = "phone_number")
    private String phoneNumber;
}
