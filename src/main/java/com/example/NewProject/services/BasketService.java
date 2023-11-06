package com.example.NewProject.services;

import com.example.NewProject.entities.Basket;
import com.example.NewProject.entities.Products;
import com.example.NewProject.entities.ProductsOnStock;
import com.example.NewProject.repos.BasketRepo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepo basketRepo;

    private final ProductOnStockService productOnStockService;

    private final ProductsService productsService;

    private final ImagesService imagesService;

    public void addProductToTheBasket(long userId, long productId, int count){
        Basket basket = new Basket();
        basket.setUserId(userId);
        basket.setProductOnStockId(productId);
        basket.setCount(count);
        basketRepo.save(basket);
    }

    @Getter
    @Setter
    private final class BasketProductShowFormat{

        private long id;

        private String image;

        private String title;

        private int count;

        private int size;

        private String cost;

        private boolean toOrder;

        private boolean onStock;

        private long productId;

    }

    private boolean checkBasketProductOnStock(Basket basket){

        return basket.getCount() <= productOnStockService
                .findProductOnStockById(basket.getProductOnStockId()).getCount();
    }

    private BasketProductShowFormat convertBasketToShowFormat(Basket basket){

        ProductsOnStock productsOnStock = productOnStockService.findProductOnStockById(basket.getProductOnStockId());
        Products product = productsService.findProductById(productsOnStock.getProductId());
        BigDecimal costOfOneProduct = new BigDecimal(product.getCost());
        BigDecimal finalSumm = costOfOneProduct.multiply(BigDecimal.valueOf(basket.getCount())).setScale(2, RoundingMode.HALF_UP);

        BasketProductShowFormat basketToReturn = new BasketProductShowFormat();
        basketToReturn.setId(basket.getId());
        basketToReturn.setImage(imagesService.getAllImagesOnProduct(product.getId()).get(0));
        basketToReturn.setTitle(product.getTitle());
        basketToReturn.setCount(basket.getCount());
        basketToReturn.setSize(productsOnStock.getSize());
        basketToReturn.setCost(finalSumm.toString());
        basketToReturn.setProductId(product.getId());
        basketToReturn.setToOrder(true);
        basketToReturn.setOnStock(checkBasketProductOnStock(basket));
        return basketToReturn;
    }

    public List<Object> findUserBasketProducts(long userId){

        List<Object> listToReturn = new ArrayList<>();
        List<Basket> usersBasket = basketRepo.findByUserId(userId);
        List<BasketProductShowFormat> basketProductShowFormat = usersBasket
                .stream()
                .map(ub -> convertBasketToShowFormat(ub))
                .collect(Collectors.toList());
        basketProductShowFormat.forEach(bpsf -> listToReturn.add(bpsf));
        return listToReturn;
    }

    public void deleteFromBasket(long basketId){

        Basket basketToDelete = basketRepo.findByBasketId(basketId);
        basketRepo.delete(basketToDelete);
    }

    public List<Basket> getBasketsByIds(List<Long> basketIds){

        List<Basket> baskets = basketIds.stream()
                .map(bi -> basketRepo.findByBasketId(bi))
                .collect(Collectors.toList());
        return baskets;
    }

}
