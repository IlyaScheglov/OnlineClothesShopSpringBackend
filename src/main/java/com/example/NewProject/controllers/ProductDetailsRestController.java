package com.example.NewProject.controllers;

import com.example.NewProject.entities.*;
import com.example.NewProject.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductDetailsRestController {

    private final ProductsService productsService;

    private final UsersService usersService;

    private final ProductOnStockService productOnStockService;

    private final BasketService basketService;

    private final LikesService likesService;

    private final ReviewsService reviewsService;

    private final OrdersService ordersService;

    private final ProductInOrderService productInOrderService;

    @GetMapping("/get-details-of-product")
    public ResponseEntity getDatailsOfProduct(@RequestParam String productId){

        long prdId = Long.parseLong(productId);

        Products product = productsService.findProductById(prdId);
        ProductsToShow productToShow = productsService.convertProductsToShowFormat(product);
        return ResponseEntity.ok(productToShow);

    }

    @GetMapping("/get-first-reviews")
    public ResponseEntity getFirstReviews(@RequestParam String productId){

        return ResponseEntity.ok(reviewsService.getProductReviews(Long.parseLong(productId)));
    }

    @GetMapping("/can-user-add-review")
    public ResponseEntity canUserAddReview(@RequestParam String productId, Principal principal){

        boolean answer = false;
        long userId = usersService.findUserByPrincipal(principal).getId();
        List<Long> ordersId = ordersService.findOrdersIdsByUserId(userId);
        List<Long> productsInOrderIds = productInOrderService.findPrdInOrdersIdsByOrderId(ordersId);
        List<Long> finalProductIds = productOnStockService.findProductIdsByProductOnStockIds(productsInOrderIds);

        if ((finalProductIds.contains(Long.parseLong(productId))) && (reviewsService
                .notRepeatReview(userId, Long.parseLong(productId)))){
            answer = true;
        }

        return ResponseEntity.ok(answer);
    }

    @GetMapping("/get-all-reviews")
    public ResponseEntity getAllReviews(@RequestParam String productId){

        List<Object> allReviews = reviewsService.findAllReviewsOnProduct(Long.parseLong(productId));
        return ResponseEntity.ok(allReviews);
    }

    @PostMapping("/add-product-to-the-basket")
    public ResponseEntity addProductToTheBasket(@RequestParam String productId, @RequestParam int sizeToOrder,
                                                @RequestParam int countToOrder, Principal principal){

        Users user = usersService.findUserByPrincipal(principal);
        long categoryIdOfProduct = productsService.findProductById(Long.parseLong(productId)).getCategory().getId();

        if(countToOrder > 0){

            if (sizeToOrder != 0){

                ProductsOnStock productOnStock = productOnStockService
                        .findProductToAddToTheBasket(Long.parseLong(productId), sizeToOrder);

                basketService.addProductToTheBasket(user, productOnStock, countToOrder);
                return ResponseEntity.ok("Товар успешно добавлен в корзину!");

            }
            else{
                return ResponseEntity.ok("Вы не выбрали размер!");
            }

        }
        else{
            return ResponseEntity.ok("Вы не выбрали количество товара!");
        }

    }

    @PostMapping("/like-or-dislike-product")
    public ResponseEntity likeOrDislikeProduct(@RequestParam String productId, Principal principal){

        Users user = usersService.findUserByPrincipal(principal);

        if (likesService.checkLikedByUser(user.getId(), Long.parseLong(productId)).size() == 0){

            likesService.addLike(user, productsService.findProductById(Long.parseLong(productId)));
            return ResponseEntity.ok("Вы добавили товар в любимое!");
        }
        else{

            likesService.deleteLike(user.getId(), Long.parseLong(productId));
            return ResponseEntity.ok("Вы убрали товар из любимого!");

        }

    }

    @PostMapping("/add-review")
    public ResponseEntity addReview(@RequestParam int stars, @RequestParam String comment,
                                    @RequestParam String productId, Principal principal){

        Users user = usersService.findUserByPrincipal(principal);

        if(stars == 0){

            return ResponseEntity.ok("noSuccess");
        }
        else{

            reviewsService.addNewReview(user, productsService.findProductById(Long.parseLong(productId)), stars, comment);
            return ResponseEntity.ok("success");
        }
    }

}
