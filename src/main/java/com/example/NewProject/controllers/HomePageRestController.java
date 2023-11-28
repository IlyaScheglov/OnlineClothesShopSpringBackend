package com.example.NewProject.controllers;

import com.example.NewProject.entities.Products;
import com.example.NewProject.services.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomePageRestController {

    private final ProductsService productsService;

    @GetMapping("/get-all-site-stock-products")
    public ResponseEntity getAllSiteStockProducts(){
        List<Products> productsOnStock = productsService.findProductsOnStock();
        return ResponseEntity.ok(productsService.convertListProductsToShowFormat(productsOnStock));
    }

    @GetMapping("/get-all-site-bestsellers")
    public ResponseEntity getAllSiteBestsellers(){
        List<Products> bestSellers = productsService.findBestSellers();
        return ResponseEntity.ok(productsService.convertListProductsToShowFormat(bestSellers));
    }

}
