package com.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;

/*
 * @author：陈晓松
 * @CLASS_NAME：DishMapper
 * @date：2023/4/22 19:26
 * @注释：
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
