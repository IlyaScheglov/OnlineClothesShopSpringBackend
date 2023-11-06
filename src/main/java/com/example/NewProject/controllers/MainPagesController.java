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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainPagesController {

    private final ProductsService productsService;

    private final UsersService usersService;


    @GetMapping("/")
    private String mainPage(Model model){

        List<Products> productsOnStock = productsService.findProductsOnStock();
        List<Products> bestSellers = productsService.findBestSellers();

        model.addAttribute("productsOnStock", productsService.convertListProductsToShowFormat(productsOnStock));
        model.addAttribute("bestSellers", productsService.convertListProductsToShowFormat(bestSellers));
        return "home_page";
    }

    @GetMapping("/shop-page")
    private String shopPage(Model model, Principal principal){
        Users user = usersService.findUserByPrincipal(principal);
        model.addAttribute("user", user);
        return "shop_page";
    }

    @GetMapping("/registration")
    private String registrationPage(Model model){
        return "registration";
    }

    @PostMapping("/registration")
    private String registerNewUser(@RequestParam String username, @RequestParam String password,
                                   @RequestParam String password2, @RequestParam String nickname,
                                   @RequestParam String email, Model model){
        if ((!username.equals("")) && (!password.equals(""))
                && (!password2.equals("")) && (!nickname.equals(""))
                && (!email.equals(""))){

            if (password.equals(password2)){

                Users user = usersService.findByUsername(username);
                if(user != null){
                    model.addAttribute("errorSameUsername", true);
                    return "registration";
                }
                else{

                    usersService.registration(username, password, nickname, email);
                    return "redirect:/login";

                }

            }
            else{
                model.addAttribute("errorNotEqualPasswords", true);
                return "registration";
            }

        }
        else{
            model.addAttribute("errorMissingText", true);
            return "registration";
        }
    }

    @GetMapping("/admin")
    private String adminPage(Model model){

        return "admin_page";
    }

    @GetMapping("/basket-page")
    private String basketPage(Model model){

        return "basket_page";
    }

    @GetMapping("/likes-page")
    private String likesPage(Model model){

        return "likes_page";
    }

    @GetMapping("/profile-page")
    private String profilePage(Model model){

        return "profile_page";
    }

    @GetMapping("/about-us")
    private String aboutUs(Model model){

        return "about_us_page";
    }

}
