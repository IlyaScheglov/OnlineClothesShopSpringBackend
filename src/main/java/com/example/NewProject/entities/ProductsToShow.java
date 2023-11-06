package com.example.NewProject.entities;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductsToShow implements Serializable{

    private long id;

    private String title;

    private String cost;

    private String smallDescription;

    private String bigDescription;

    private long colorId;

    private long categoryId;

    private int countReviews;

    private List<Integer> sizes = new ArrayList<>();

    private List<String> images = new ArrayList<>();

    private List<String> stars = new ArrayList<>();
}
