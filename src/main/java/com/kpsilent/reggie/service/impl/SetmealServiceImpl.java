package com.kpsilent.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kpsilent.reggie.common.CustomException;
import com.kpsilent.reggie.dto.SetmealDto;
import com.kpsilent.reggie.entity.Setmeal;
import com.kpsilent.reggie.entity.SetmealDish;
import com.kpsilent.reggie.mapper.SetmealMapper;
import com.kpsilent.reggie.service.SetmealDishService;
import com.kpsilent.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 保存套餐信息并保存菜品信息
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // 插入套餐信息
        this.save(setmealDto);

        // 插入套餐菜品信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        // 遍历集合，设置菜品所对应的套餐id
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐和与套餐相关联的菜品
     * @param ids
     */
    @Transactional
    public void removeWithDish(List<Long> ids){
        // 查询套餐是否停售，如果停售可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);
        // 如果被删除的套餐中有未停售的，则不能删除，并且抛出异常
        if(count > 0){
            throw new CustomException("套餐正在出售，不能删除");
        }

        // 删除套餐
        this.removeByIds(ids);

        // 删除套餐所对应的菜品
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(queryWrapper1);
    }
}
