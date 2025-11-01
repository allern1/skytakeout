package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);



    // 插入菜品数据
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    // 查询菜品数据
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    // 根据id查询菜品和对应的口味数据
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    // 根据id删除菜品数据
    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    // 根据id批量删除菜品
    void deleteByIds(List<Long> ids);

    // 修改菜品基本数据
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);
}
