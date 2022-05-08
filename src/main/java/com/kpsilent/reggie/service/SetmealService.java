package com.kpsilent.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kpsilent.reggie.dto.SetmealDto;
import com.kpsilent.reggie.entity.Setmeal;
import java.util.List;
public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐和与套餐相关联的菜品
     * @param ids
     */
    public void removeWithDish(List<Long> ids);
}
