package com.reggie.service.impl;

/*
 * @author：陈晓松
 * @CLASS_NAME：DishServiceImpl
 * @date：2023/4/22 19:30
 * @注释：
 */

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.dto.DishDto;
import com.reggie.mapper.DishMapper;
import com.reggie.pojo.Dish;
import com.reggie.pojo.DishFlavor;
import com.reggie.service.DishFlavorService;
import com.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Override
    @Transactional // 声明事务性方法的注解,涉及多张表的操作需要加入事务控制
    public void saveWithFlavor(DishDto dishDto) {
        // 保存菜品基本信息到菜品表：dish
        this.save(dishDto);
        // 菜品ID
        Long dishId = dishDto.getId();
        // 菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        // 保存菜品口味信息到口味表：dish_flavor
        dishFlavorService.saveBatch(flavors);
    }
}