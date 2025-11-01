package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    // 根据套餐id查询菜品

    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
}
