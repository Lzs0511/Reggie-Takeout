package com.kpsilent.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kpsilent.reggie.entity.OrderDetail;
import com.kpsilent.reggie.mapper.OrderDetailMapper;
import com.kpsilent.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
