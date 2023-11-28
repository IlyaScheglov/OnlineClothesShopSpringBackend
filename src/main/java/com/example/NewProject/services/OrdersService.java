package com.example.NewProject.services;

import com.example.NewProject.entities.Orders;
import com.example.NewProject.entities.Users;
import com.example.NewProject.repos.OrdersRepo;
import jakarta.persistence.criteria.Order;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepo ordersRepo;

    private final StatusesService statusesService;

    private final ProductInOrderService productInOrderService;

    private final ProductOnStockService productOnStockService;

    private final ImagesService imagesService;

    public List<Long> findOrdersIdsByUserId(long userId){

        List<Orders> orders = ordersRepo.findByUsId(userId);
        List<Long> ordersId = orders.stream().map(o -> o.getId()).collect(Collectors.toList());
        return ordersId;
    }

    public Orders makeOrder(Users user, String address, String cost, String fio, String phone){

        String finalFio, finalPhone;
        if (fio.length() > 100){
            finalFio = fio.substring(0, 100);
        }
        else {
            finalFio = fio;
        }
        if (phone.length() > 16){
            finalPhone = phone.substring(0, 16);
        }
        else {
            finalPhone = phone;
        }

        Orders order = new Orders();
        order.setUser(user);
        order.setAddress(address);
        order.setStatus(statusesService.getStatusById(1L));
        order.setCost(cost);
        order.setFio(finalFio);
        order.setPhoneNumber(finalPhone);
        ordersRepo.save(order);
        return order;
    }

    public Orders findOrderById(long orderId){

        return ordersRepo.findByOrderId(orderId);
    }

    public List<Object> getUserOrders(long userId){

        List<Orders> orders = ordersRepo.findByUsId(userId);
        Collections.reverse(orders);
        return convertOrdersToShowFormat(orders);
    }

    @Getter
    @Setter
    private final class OrdersShowFormat{

        private long id;

        private String image;

        private String status;

        private String address;

        private String finalAddress;

        private int count;

        private String cost;
    }

    public List<Object> convertOrdersToShowFormat(List<Orders> orders){

        List<Object> result = new ArrayList<>();
        orders.forEach(o -> {
            long productOnStockId = productInOrderService.getFirstProductOnStockId(o.getId());
            long productId = productOnStockService.findProductIdByStockId(productOnStockId);

            OrdersShowFormat ordersShowFormat = new OrdersShowFormat();
            ordersShowFormat.setId(o.getId());
            ordersShowFormat.setImage(imagesService.getFirstImageOfProduct(productId));
            ordersShowFormat.setAddress(o.getAddress());
            ordersShowFormat.setFinalAddress(o.getFinalAddress());
            ordersShowFormat.setCost(o.getCost());
            ordersShowFormat.setStatus(o.getStatus().getTitle());
            ordersShowFormat.setCount(productInOrderService.getCountOfProductsInOrder(o.getId()));
            result.add(ordersShowFormat);
        });
        return result;
    }

    public List<Orders> findAllOrders(){

        return ordersRepo.findAll();
    }

    public void changeOrderStatus(long orderId, String address){

        Orders order = ordersRepo.findByOrderId(orderId);
        if(order.getStatus().getId() == 1L){
            order.setFinalAddress(address);
            order.setStatus(statusesService.getStatusById(2L));
            ordersRepo.save(order);
        }
        else if(order.getStatus().getId() == 2L){
            order.setStatus(statusesService.getStatusById(3L));
            ordersRepo.save(order);
        }
    }
}
