package com.kpsilent.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kpsilent.reggie.common.CustomException;
import com.kpsilent.reggie.entity.Category;
import com.kpsilent.reggie.entity.Dish;
import com.kpsilent.reggie.entity.Setmeal;
import com.kpsilent.reggie.mapper.CategoryMapper;
import com.kpsilent.reggie.service.CategoryService;
import com.kpsilent.reggie.service.DishService;
import com.kpsilent.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        // 添加查询条件，根据分类id进行查询
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int cnt1 = dishService.count(dishLambdaQueryWrapper);
        // 查询当前分类是否已经关联了菜品， 如果关联了则抛出异常
        if(cnt1 > 0){
            throw new CustomException("当前分类下关联了菜品，不能删除！");
        }

        // 添加当前分类是否已经关联了套餐， 如果关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.eq(Setmeal::getCategoryId, id);
        int cnt2 = setmealService.count(setmealQueryWrapper);
        if(cnt2 > 0){
            throw new CustomException("当前分类下关联了套餐，不能删除！ ");
        }

        // 没有套餐或者菜品关联当前分类则删除当前分类
        super.removeById(id);
    }
}
