package com.example.NewProject.controllers;

import com.example.NewProject.services.OrdersService;
import com.example.NewProject.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProfilePageRestController {

    private final OrdersService ordersService;

    private final UsersService usersService;

    @GetMapping("/get-all-user-orders")
    public ResponseEntity getAllUserOrders(Principal principal){

        long userId = usersService.findUserByPrincipal(principal).getId();
        List<Object> orders = ordersService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }
}
