package com.example.NewProject.services;

import com.example.NewProject.entities.Orders;
import com.example.NewProject.entities.ProductsInOrder;
import com.example.NewProject.entities.ProductsOnStock;
import com.example.NewProject.repos.ProductsInOrderRepo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductInOrderService {

    private final ProductsInOrderRepo productsInOrderRepo;

    private final ProductOnStockService productOnStockService;

    public List<Long> findAllSelledProductsOnStockIds(){

        List<Long> result = new ArrayList<>();

        List<ProductsInOrder> productsInOrders = productsInOrderRepo.findAll();

        productsInOrders.forEach(pio -> result.add(pio.getProductsOnStock().getId()));

        return result;
    }

    public List<Long> findPrdInOrdersIdsByOrderId(List<Long> orderIds){

        List<ProductsInOrder> productsInOrders = new ArrayList<>();
        orderIds.forEach(oi -> {

            List<ProductsInOrder> prdInOrder = productsInOrderRepo.findByOrdId(oi);
            prdInOrder.forEach(prio -> productsInOrders.add(prio));
        });

        List<Long> result = productsInOrders.stream().map(pio -> pio.getProductsOnStock().getId()).collect(Collectors.toList());
        return result;
    }

    public void addProductToOrder(Orders order, ProductsOnStock productOnStock, int count){

        ProductsInOrder productsInOrder = new ProductsInOrder();
        productsInOrder.setOrder(order);
        productsInOrder.setProductsOnStock(productOnStock);
        productsInOrder.setCount(count);
        productsInOrderRepo.save(productsInOrder);
    }

    public int getCountOfProductsInOrder(long orderId){

        List<ProductsInOrder> productsInOrder = productsInOrderRepo.findByOrdId(orderId);
        int count = productsInOrder.stream().mapToInt(pio -> pio.getCount()).sum();
        return count;
    }

    public long getFirstProductOnStockId(long orderId){

        return productsInOrderRepo.findByOrdId(orderId).get(0).getProductsOnStock().getId();
    }

    public List<Object> findProductsInOrderToAdminByOrderId(long orderId){

        List<ProductsInOrder> productsInOrder = productsInOrderRepo.findByOrdId(orderId);
        return convertToShowFormat(productsInOrder);
    }

    @Getter
    @Setter
    private final class PrdInOrdShowFormat{

        private long id;

        private long productId;

        private int size;

        private int count;
    }

    private List<Object> convertToShowFormat(List<ProductsInOrder> productsInOrder){

        List<Object> prdInOrdShowFormat = new ArrayList<>();
        productsInOrder.forEach(pio -> {

            PrdInOrdShowFormat prd = new PrdInOrdShowFormat();
            prd.setId(pio.getId());
            prd.setCount(pio.getCount());
            prd.setSize(pio.getProductsOnStock().getSize());
            prd.setProductId(productOnStockService.findProductIdByStockId(pio.getProductsOnStock().getId()));
            prdInOrdShowFormat.add(prd);
        });
        return prdInOrdShowFormat;
    }


}
