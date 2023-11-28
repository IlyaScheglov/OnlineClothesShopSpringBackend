package com.example.NewProject.controllers;

import com.example.NewProject.entities.Orders;
import com.example.NewProject.entities.Users;
import com.example.NewProject.services.OrdersService;
import com.example.NewProject.services.ProductInOrderService;
import com.example.NewProject.services.SendingEmails;
import com.example.NewProject.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AdminOrdersPageRestController {

    private final OrdersService ordersService;

    private final ProductInOrderService productInOrderService;

    private final UsersService usersService;

    private final SendingEmails sendingEmails;

    @GetMapping("/get-all-orders-to-admin")
    public ResponseEntity getAllOrdersToAdmin(@RequestParam String search){

        List<Orders> allOrders = ordersService.findAllOrders();
        List<Object> result = new ArrayList<>();
        if (search.equals("")){
            result = ordersService.convertOrdersToShowFormat(allOrders);
        }
        else if(StringUtils.isNumeric(search)){
            List<Orders> filteredOrders = allOrders.stream()
                    .filter(fo -> fo.getId() == Long.parseLong(search)).collect(Collectors.toList());
            result = ordersService.convertOrdersToShowFormat(filteredOrders);
        }
        else {
            result = result;
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get-all-products-in-order-to-admin")
    public ResponseEntity getAllProductsInOrderToAdmin(@RequestParam int orderId){

        return ResponseEntity.ok(productInOrderService.findProductsInOrderToAdminByOrderId((long)orderId));
    }

    @PutMapping("/change-status-of-order-by-admin")
    public ResponseEntity changeStatusOfOrderByAdmin(@RequestParam int orderId, @RequestParam String address){

        String resultMessage = "";
        List<Object> result = new ArrayList<>();
        try {
            ordersService.changeOrderStatus((long) orderId, address);
            resultMessage = "Статус изменен";
        }
        catch (Exception e){
            e.printStackTrace();
            resultMessage = "Что-то пошло не так";
        }
        result.add(resultMessage);
        result.add(orderId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/send-messages-changed-status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendMessagesChangedStatus(@RequestParam int orderId){

        Orders order = ordersService.findOrderById((long)orderId);
        Users user = usersService.findUserById(order.getUser().getId());
        sendingEmails.sendMailToUser(user, order, (int)order.getStatus().getId());
    }


}
