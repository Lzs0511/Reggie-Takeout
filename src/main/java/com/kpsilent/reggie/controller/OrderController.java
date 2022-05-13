package com.kpsilent.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.kpsilent.reggie.common.BaseContext;
import com.kpsilent.reggie.common.CustomException;
import com.kpsilent.reggie.common.R;
import com.kpsilent.reggie.entity.AddressBook;
import com.kpsilent.reggie.entity.Orders;
import com.kpsilent.reggie.entity.ShoppingCart;
import com.kpsilent.reggie.entity.User;
import com.kpsilent.reggie.service.AddressBookService;
import com.kpsilent.reggie.service.OrderService;
import com.kpsilent.reggie.service.ShoppingCartService;
import com.kpsilent.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单信息{}", orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }
}
