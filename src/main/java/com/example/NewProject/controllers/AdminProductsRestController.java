package com.example.NewProject.controllers;

import com.example.NewProject.entities.Products;
import com.example.NewProject.entities.ProductsToShow;
import com.example.NewProject.services.ProductOnStockService;
import com.example.NewProject.services.ProductsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AdminProductsRestController {

    private final ProductsService productsService;

    private final ProductOnStockService productOnStockService;

    @GetMapping("/get-all-products-to-admin")
    public ResponseEntity getAllProductsToAdmin(@RequestParam String search){

        List<ProductsToShow> result = new ArrayList<>();
        List<Products> allProducts = productsService.findAllProducts();
        if(search.equals("")){
            result = productsService.convertListProductsToShowFormat(allProducts);
        }
        else if(StringUtils.isNumeric(search)){
            List<Products> filteredByIdProducts = allProducts.stream()
                    .filter(ap -> ap.getId() == Long.parseLong(search))
                    .collect(Collectors.toList());
            result = productsService.convertListProductsToShowFormat(filteredByIdProducts);
        }
        else{
            List<Products> filterdByTitleProducts = allProducts.stream()
                    .filter(ap -> ap.getTitle().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
            result = productsService.convertListProductsToShowFormat(filterdByTitleProducts);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get-main-product-to-admin")
    public ResponseEntity getMainProductToAdmin(@RequestParam int productId){

        Products product = productsService.findProductById((long)productId);
        return ResponseEntity.ok(productsService.convertProductsToShowFormat(product));
    }

    @PutMapping("/add-product-to-the-stock")
    public ResponseEntity addProductToTheStock(@RequestParam int productId, @RequestParam int productSize,
                                               @RequestParam int productCount){

        try{
            productOnStockService.addMoreCountsOfProduct((long)productId, productSize, productCount);
            return ResponseEntity.ok("Вы добавили товар на склад!");
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok("Что-то пошло не так!");
        }
    }


}
