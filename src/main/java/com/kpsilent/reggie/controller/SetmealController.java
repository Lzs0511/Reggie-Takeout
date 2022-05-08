package com.kpsilent.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kpsilent.reggie.common.R;
import com.kpsilent.reggie.dto.DishDto;
import com.kpsilent.reggie.dto.SetmealDto;
import com.kpsilent.reggie.entity.Category;
import com.kpsilent.reggie.entity.Setmeal;
import com.kpsilent.reggie.entity.SetmealDish;
import com.kpsilent.reggie.service.CategoryService;
import com.kpsilent.reggie.service.SetmealDishService;
import com.kpsilent.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("setmealDto:{}", setmealDto.toString());
        setmealService.saveWithDish(setmealDto);
        return R.success("添加套餐成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        // 构造分页构造器
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();
        BeanUtils.copyProperties(setmealPage, dtoPage);
        // 构造查询条件
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);

        setmealService.page(setmealPage, queryWrapper);
        // 获取分页信息中套餐数据的集合
        List<Setmeal> records = setmealPage.getRecords();

        // 将套餐集合中的数据拷贝到dtoPage的records中

        List<SetmealDto> dtoRecords = records.stream().map((item)->{
            SetmealDto dto = new SetmealDto();
            Category category = categoryService.getById(item.getCategoryId());
            // 查询套餐分类名称
            if(category != null)
                dto.setCategoryName(category.getName());
            // 将套餐Setmeal的数据拷贝到dto中
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());

        // 将dtoRecords存入到dtoPage中
        dtoPage.setRecords(dtoRecords);
        return R.success(dtoPage);

    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids:{}", ids);
        setmealService.removeWithDish(ids);
        return R.success("删除成功");
    }
}
