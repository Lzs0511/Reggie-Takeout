package com.kpsilent.reggie.dto;


import com.kpsilent.reggie.entity.Setmeal;
import com.kpsilent.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
