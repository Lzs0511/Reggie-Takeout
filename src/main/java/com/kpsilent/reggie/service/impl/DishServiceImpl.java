package com.kpsilent.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kpsilent.reggie.dto.DishDto;
import com.kpsilent.reggie.entity.Dish;
import com.kpsilent.reggie.entity.DishFlavor;
import com.kpsilent.reggie.mapper.DishMapper;
import com.kpsilent.reggie.service.DishFlavorService;
import com.kpsilent.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 新增菜品，同时保存口味数据
     * @param dishDto
     */
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        // 1. 保存菜品数据
        this.save(dishDto);

        // 2. 保存口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        Long categoryId = dishDto.getId();
        flavors.stream().map((item) -> {
            item.setDishId(categoryId);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dto) {
        // 1. 先更新dto中Dish部分的数据
        this.updateById(dto);
        // 2. 删除flavor中相应的数据
        // 构造删除条件
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dto.getId());
        dishFlavorService.remove(queryWrapper);
        // 3. 将dto中flavor数据写回到数据库中
        List<DishFlavor> flavors = dto.getFlavors();
        // 因为DishFlavor中没有DishId，遍历集合将DishId添加进去然后返回
        flavors.stream().map((item) ->{
            item.setDishId(dto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);

    }

    // 通过id查询菜品信息并且查询口味信息
    public DishDto getByIdWithFlavors(Long id){
        Dish dish = this.getById(id);
        DishDto dto = new DishDto();
        // 将dish中的数据拷贝到dto中
        BeanUtils.copyProperties(dish, dto);

        // 通过菜品id查询flavor(口味)信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        // 将口味信息添加到dto中
        dto.setFlavors(flavors);

        return dto;
    }
}
