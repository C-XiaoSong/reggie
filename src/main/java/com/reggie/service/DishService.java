package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.dto.DishDto;
import com.reggie.pojo.Dish;

/**
 * @author： 陈晓松
 * @CLASS_NAME： DishService
 * @date： 2023/4/22 19:29
 * @注释： 两张表的操作：dish、dish_flavor
 */
public interface DishService extends IService<Dish> {
    // 新增菜品，同时插入菜品对应的口味数据，需要操作两张表：dish、dish_flavor
    void saveWithFlavor(DishDto dishDto);
}
