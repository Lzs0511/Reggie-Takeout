package com.kpsilent.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kpsilent.reggie.entity.Category;
import com.kpsilent.reggie.mapper.CategoryMapper;
import com.kpsilent.reggie.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
