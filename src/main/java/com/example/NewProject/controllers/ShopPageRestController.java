package com.example.NewProject.controllers;

import com.example.NewProject.entities.Categories;
import com.example.NewProject.entities.Products;
import com.example.NewProject.entities.ProductsToShow;
import com.example.NewProject.services.CategoriesService;
import com.example.NewProject.services.ProductOnStockService;
import com.example.NewProject.services.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ShopPageRestController {

    private final ProductsService productsService;

    private final CategoriesService categoriesService;

    private final ProductOnStockService productOnStockService;

    @GetMapping("/get-filtered-products")
    public ResponseEntity<Object> filterProducts(@RequestParam int sortingType, @RequestParam int costFilter,
                                                 @RequestParam int colorFilter, @RequestParam int categoryFilter,
                                                 @RequestParam int sizeFilter, @RequestParam String searchFilter){

        List<Object> result = new ArrayList<>();
        List<Products> allProducts = productsService.findAllProducts();
        List<Products> filteredAndSortedProducts = productsService.filterProducts(sortingType, costFilter, colorFilter,
                categoryFilter, sizeFilter, searchFilter, allProducts);

        if (filteredAndSortedProducts.size() > 6){

            List<Products> limitedProducts = filteredAndSortedProducts.stream().limit(6)
                    .collect(Collectors.toList());
            limitedProducts.forEach(lp -> result.add(productsService.convertProductsToShowFormat(lp)));
            result.add(true);

        }
        else{

            filteredAndSortedProducts.forEach(fasp -> result.add(productsService.convertProductsToShowFormat(fasp)));
            result.add(false);

        }

        return ResponseEntity.ok(result);
    }


    @GetMapping("/load-more")
    public ResponseEntity<Object> loadMoreProducts(@RequestParam int sortingType, @RequestParam int costFilter,
                                                   @RequestParam int colorFilter, @RequestParam int categoryFilter,
                                                   @RequestParam int sizeFilter, @RequestParam String searchFilter,
                                                   @RequestParam int[] productsIdsList){

        List<Object> result = new ArrayList<>();

        List<Long> loadedIds = new ArrayList<>();
        for(int i = 0; i < productsIdsList.length; i++){
            loadedIds.add(Long.parseLong(String.valueOf(productsIdsList[i])));
        }
        List<Products> allProducts = productsService.findAllProducts();

        loadedIds.forEach(li -> allProducts.remove(productsService.findProductById(li)));

        List<Products> filteredAndSortedProducts = productsService.filterProducts(sortingType, costFilter, colorFilter,
                categoryFilter, sizeFilter, searchFilter, allProducts);

        if (filteredAndSortedProducts.size() > 6){

            List<Products> limitedProducts = filteredAndSortedProducts.stream().limit(6)
                    .collect(Collectors.toList());
            limitedProducts.forEach(lp -> result.add(productsService.convertProductsToShowFormat(lp)));
            result.add(true);

        }
        else{

            filteredAndSortedProducts.forEach(fasp -> result.add(productsService.convertProductsToShowFormat(fasp)));
            result.add(false);

        }

        return ResponseEntity.ok(result);

    }

    @GetMapping("/get-all-categories")
    public ResponseEntity getAllCategories(){

        return ResponseEntity.ok(categoriesService.findAllCategories());
    }

    @GetMapping("/get-all-sizes")
    public ResponseEntity getAllSizes(){

        return ResponseEntity.ok(productOnStockService.findAllSizes());
    }

}
