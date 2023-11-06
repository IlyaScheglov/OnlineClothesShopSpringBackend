package com.example.NewProject.controllers;

import com.example.NewProject.entities.Products;
import com.example.NewProject.entities.ProductsToShow;
import com.example.NewProject.entities.Users;
import com.example.NewProject.services.ProductsService;
import com.example.NewProject.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AdditionalPagesController {

    private final UsersService usersService;

    private final ProductsService productsService;

    @GetMapping("/shop-page/{productId}")
    private String productDetails(@PathVariable("productId") long productId, Model model){

        model.addAttribute("prdId", productId);
        return "product_details_page";

    }

    @GetMapping("/admin/new-product")
    private String adminNewProduct(Model model){

        return "new_product_page";
    }

    @GetMapping("/admin/products-list")
    private String adminProductsList(Model model){

        return "adm_products_page";
    }

    @GetMapping("/admin/orders-list")
    private String adminOrdersList(Model model){

        return "adm_orders_page";
    }


}
