package com.example.NewProject.controllers;

import com.example.NewProject.entities.Basket;
import com.example.NewProject.entities.Orders;
import com.example.NewProject.entities.Users;
import com.example.NewProject.services.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequiredArgsConstructor
public class BasketPageRestController {

    private final BasketService basketService;

    private final UsersService usersService;

    private final ProductOnStockService productOnStockService;

    private final OrdersService ordersService;

    private final ProductInOrderService productInOrderService;

    private final TelegramBot telegramBot;

    private final SendingEmails sendingEmails;

    @GetMapping("/get-basket-products")
    public ResponseEntity getBasketProducts(Principal principal){

        long userId = usersService.findUserByPrincipal(principal).getId();
        return ResponseEntity.ok(basketService.findUserBasketProducts(userId));
    }

    @GetMapping("/get-all-basket-cost")
    public ResponseEntity getAllBasketCost(@RequestParam String[] allCosts){

        if ((allCosts.length == 1) && (allCosts[0].equals("0.00"))){
            return ResponseEntity.ok("0.00");
        }
        else{
            List<String> allCostsList = Arrays.stream(allCosts).toList();
            double summInDouble = allCostsList.stream().mapToDouble(acl -> Double.parseDouble(acl)).sum();
            String notFinalSumm = "" + summInDouble;
            BigDecimal summ = new BigDecimal(notFinalSumm).setScale(2, RoundingMode.HALF_UP);
            return ResponseEntity.ok(summ.toString());
        }
    }

    @DeleteMapping("/delete-product-from-basket")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductFromBasket(@RequestParam long basketId){

        basketService.deleteFromBasket(basketId);
    }

    @PostMapping("/order-products")
    public ResponseEntity orderProducts(@RequestParam int[] basketIds, @RequestParam String costOfOrder,
                                        @RequestParam String address, @RequestParam String fio,
                                        @RequestParam String phone, Principal principal){

        List<Object> result = new ArrayList<>();

        if ((basketIds.length == 1) && (basketIds[0] == 0)){
            result.add("Вы не выбрали товары для заказа!");
            result.add(false);
        }
        else{
            List<Long> basketIdsList = new ArrayList<>();
            for (int i = 0; i < basketIds.length; i++){
                basketIdsList.add((long)basketIds[i]);
            }
            List<Basket> basketsToOrder = basketService.getBasketsByIds(basketIdsList);
            if (costOfOrder.equals("0.00")){
                result.add("Вы не выбрали товары для заказа!");
                result.add(false);
            }
            else if(address.equals("")){
                result.add("Вы не ввели адрес!");
                result.add(false);
            }
            else if((checkProductsOnStockToOrder(basketsToOrder) == false) &&
                    (Integer.parseInt(costOfOrder.substring(0, costOfOrder.length() - 3)) < 2000)){
                result.add("Если вы заказываете товары, которых нет на складе, стоимость должна быть не меньше 2000.00₽!");
                result.add(false);
            }
            else{
                Users user = usersService.findUserByPrincipal(principal);
                Orders order = ordersService.makeOrder(user.getId(), address, costOfOrder, fio, phone);
                basketsToOrder.forEach(bto -> {
                    productInOrderService.addProductToOrder(order.getId(), bto.getProductOnStockId(), bto.getCount());
                    productOnStockService.minusProductsFromStock(bto.getProductOnStockId(), bto.getCount());
                    basketService.deleteFromBasket(bto.getId());
                });
                result.add("Заказ оформлен! Проверяйте его статус в профиле и на почте!");
                result.add(order);
            }
        }
        return ResponseEntity.ok(result);
    }

    private boolean checkProductsOnStockToOrder(List<Basket> baskets){

        AtomicBoolean answer = new AtomicBoolean(true);
        List<BasketShort> basketShorts = convertBasketToShortFormat(baskets);
        basketShorts.forEach(bs -> {

            int countInThisBasket = basketShorts.stream()
                    .filter(bss -> bss.getProductOnStockId() == bs.getProductOnStockId())
                    .mapToInt(bss -> bss.getCount())
                    .sum();
            if (countInThisBasket > productOnStockService
                    .findProductOnStockById(bs.getProductOnStockId()).getCount()){
                answer.set(false);
            }
        });
        return answer.get();
    }

    @Getter
    @Setter
    private final class BasketShort{

        private int index;

        private long productOnStockId;

        private int count;
    }

    private List<BasketShort> convertBasketToShortFormat(List<Basket> baskets){

        List<BasketShort> basketsShort = new ArrayList<>();
        baskets.forEach(b -> {

            BasketShort basketShort = new BasketShort();
            basketShort.setIndex(baskets.indexOf(b));
            basketShort.setProductOnStockId(b.getProductOnStockId());
            basketShort.setCount(b.getCount());
            basketsShort.add(basketShort);
        });
        return basketsShort;
    }

    @PostMapping("/send-messages")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendMessages(@RequestParam long orderId, Principal principal){

        Orders order = ordersService.findOrderById(orderId);
        Users user = usersService.findUserByPrincipal(principal);
        telegramBot.sendOrder(order.getId());
        sendingEmails.sendMailToUser(user, order, 1);
    }
}
