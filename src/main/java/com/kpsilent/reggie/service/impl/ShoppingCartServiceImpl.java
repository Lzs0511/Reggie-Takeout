package com.kpsilent.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kpsilent.reggie.entity.ShoppingCart;
import com.kpsilent.reggie.mapper.ShoppingCartMapper;
import com.kpsilent.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl
        extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
        implements ShoppingCartService {


}
