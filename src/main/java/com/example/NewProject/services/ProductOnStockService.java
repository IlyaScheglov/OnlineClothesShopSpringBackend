package com.example.NewProject.services;

import com.example.NewProject.entities.ProductsInOrder;
import com.example.NewProject.entities.ProductsOnStock;
import com.example.NewProject.repos.ProductsOnStockRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductOnStockService {

    private final ProductsOnStockRepo productsOnStockRepo;


    public List<Long> getProductIdWhoOnStock(){

        List<ProductsOnStock> productsOnStock = productsOnStockRepo.findAll();
        List<Long> allProductsIds = productsOnStock.stream()
                .filter(pos -> pos.getCount() > 0)
                .map(pos -> pos.getProductId())
                .distinct()
                .collect(Collectors.toList());
        return allProductsIds;

    }

    public List<Long> findBestSellersProductIds(List<Long> productsOnStockIds){

        List<Long> result = new ArrayList<>();

        productsOnStockIds.forEach(posi -> result.add(productsOnStockRepo.findByProductOnStockId(posi)
                .getProductId()));

        return result;

    }

    public List<Long> findProductIdBySize(int size){

        return productsOnStockRepo.findProductIdsBySize(size);

    }


    public List<Integer> findSizesOfProduct(long productId){

        List<ProductsOnStock> productsOnStockWithThisId = productsOnStockRepo.findByProductId(productId);

        List<Integer> listOfSizes = productsOnStockWithThisId.stream().map(poswti -> poswti.getSize())
                .collect(Collectors.toList());

        return listOfSizes;

    }

    public ProductsOnStock findProductToAddToTheBasket(long productId, int size){

        return productsOnStockRepo.findByProductIdAndSize(productId, size);

    }

    public List<Long> findProductIdsByProductOnStockIds(List<Long> productIds){

        List<ProductsOnStock> productsOnStocks = new ArrayList<>();
        productIds.forEach(pi -> {

            List<ProductsOnStock> prdOnStock = productsOnStockRepo.findByItsId(pi);
            prdOnStock.forEach(prio -> productsOnStocks.add(prio));
        });

        List<Long> result = productsOnStocks.stream().map(pio -> pio.getProductId()).collect(Collectors.toList());
        return result;
    }

    public List<Integer> findAllSizes(){

        List<ProductsOnStock> allProducts = productsOnStockRepo.findAll();
        List<Integer> result = allProducts.stream()
                .map(ap -> ap.getSize())
                .distinct().collect(Collectors.toList());
        return result;
    }

    public ProductsOnStock findProductOnStockById(long productOnStockId){

        return productsOnStockRepo.findByProductOnStockId(productOnStockId);
    }

    public void minusProductsFromStock(long productOnStockId, int countToMinus){

        ProductsOnStock productsOnStock = findProductOnStockById(productOnStockId);
        productsOnStock.setCount(productsOnStock.getCount() - countToMinus);
        productsOnStockRepo.save(productsOnStock);
    }

    public long findProductIdByStockId(long productOnStockId){

        return productsOnStockRepo.findByProductOnStockId(productOnStockId).getProductId();
    }

    public void addProductToStock(long productId, int size){

            ProductsOnStock productsOnStock = new ProductsOnStock();
            productsOnStock.setProductId(productId);
            productsOnStock.setCount(0);
            productsOnStock.setSize(size);
            productsOnStockRepo.save(productsOnStock);
    }

    public void addMoreCountsOfProduct(long productId, int size, int count){

        ProductsOnStock productsOnStock = productsOnStockRepo.findByProductIdAndSize(productId, size);
        productsOnStock.setCount(count);
        productsOnStockRepo.save(productsOnStock);
    }
}
