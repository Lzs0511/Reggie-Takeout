package com.kpsilent.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kpsilent.reggie.common.R;
import com.kpsilent.reggie.dto.DishDto;
import com.kpsilent.reggie.entity.Category;
import com.kpsilent.reggie.entity.Dish;
import com.kpsilent.reggie.service.CategoryService;
import com.kpsilent.reggie.service.DishFlavorService;
import com.kpsilent.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    private R<String> save(@RequestBody DishDto dto){
        log.info(dto.toString());
        dishService.saveWithFlavor(dto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    private R<Page> page(int page, int pageSize, String name){
        // 创建分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        // 构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        // 执行分页查询
        dishService.page(pageInfo, queryWrapper);

        // 首先把Dish数据集合之外的属性拷贝到dishDtoPage中
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dtoList = records.stream().map((item) ->{
            DishDto dto = new DishDto();
            // 将Dish中的数据拷贝到DishDto中
            BeanUtils.copyProperties(item, dto);
            // 通过菜品分类id查询出菜品分类名称，并存入到dto中
            Category category = categoryService.getById(item.getCategoryId());
            if(category != null){
                dto.setCategoryName(category.getName());
            }
            return dto;
        }).collect(Collectors.toList());

        // 将数据dtoList存入到分页信息中
        dishDtoPage.setRecords(dtoList);

        return R.success(dishDtoPage);
    }
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){

        DishDto dto = dishService.getByIdWithFlavors(id);
        return R.success(dto);
    }
    @PutMapping
    public R<String> update(@RequestBody DishDto dto){
        dishService.updateWithFlavor(dto);
        return R.success("修改菜品成功");
    }

    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        // 1. 根据Dish中的id查询菜品并返回
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        // 查询起售的菜品
        queryWrapper.eq(Dish::getStatus, 1);
        // 根据sort字段升序和更新时间updateTime降序排序
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> dishes = dishService.list(queryWrapper);

        return R.success(dishes);
    }
}
