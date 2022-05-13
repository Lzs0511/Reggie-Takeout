package com.kpsilent.reggie.controller;

import com.kpsilent.reggie.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;


}
