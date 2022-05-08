package com.kpsilent.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kpsilent.reggie.dto.DishDto;
import com.kpsilent.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    // 通过id查询菜品信息并且查询口味信息
    public DishDto getByIdWithFlavors(Long id);
    public void saveWithFlavor(DishDto dishDto);

    public void updateWithFlavor(DishDto dto);
}
