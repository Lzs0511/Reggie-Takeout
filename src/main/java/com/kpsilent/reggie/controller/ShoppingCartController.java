package com.kpsilent.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kpsilent.reggie.common.BaseContext;
import com.kpsilent.reggie.common.R;
import com.kpsilent.reggie.entity.ShoppingCart;
import com.kpsilent.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加商品到购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        // 将用户id放入到shoppingCart中
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        // 判断添加的商品是套餐还是菜品
        Long dishId = shoppingCart.getDishId();

        // 构造条件
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);

        if(dishId != null){
            // 添加的是菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        }else {
            // 添加的是套餐
            Long setmealId = shoppingCart.getSetmealId();
            queryWrapper.eq(ShoppingCart::getSetmealId, setmealId);
        }
        // 查询当前商品是否在购物车中（在数据库中），如果在则更新数量，没有则插入商品
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);
        if(one != null){
            // 更新商品数量为原数量加一
            Integer number = one.getNumber();
            one.setNumber(number + 1);
            shoppingCartService.updateById(one);
        } else{
            // 将商品插入到数据库中
            one = shoppingCart;
            one.setNumber(1);
            one.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(one);
        }

        return R.success(one);
    }

    /**
     * 展示购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("展示购物车数据");
        // 构造条件，查询用户购物车中的商品
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        List<ShoppingCart> shoppings = shoppingCartService.list(queryWrapper);
        return R.success(shoppings);
    }

    @DeleteMapping("/clean")
    public R<String> delete(){
        log.info("清空购物车");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空成功");
    }
}
