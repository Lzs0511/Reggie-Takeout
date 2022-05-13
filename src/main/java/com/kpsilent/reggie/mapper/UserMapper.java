package com.kpsilent.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kpsilent.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
