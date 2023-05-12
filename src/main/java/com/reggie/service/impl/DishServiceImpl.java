package com.reggie.service.impl;

/*
 * @author：陈晓松
 * @CLASS_NAME：DishServiceImpl
 * @date：2023/4/22 19:30
 * @注释：
 */

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.dto.DishDto;
import com.reggie.mapper.DishMapper;
import com.reggie.pojo.Dish;
import com.reggie.pojo.DishFlavor;
import com.reggie.service.DishFlavorService;
import com.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品（两张表操作）
     * @param dishDto
     */
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

    /**
     * 根据ID查询菜品信息以及口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        // 查询菜品基本信息，dish这个表
        Dish dish = this.getById(id);

        DishDto dto = new DishDto();
        BeanUtils.copyProperties(dish,dto); // 把dish里面的属性拷贝到dto里面

        // 查询当前菜品对应的口味信息，dish_flavor这个表
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<DishFlavor>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        // 设置一下集合
        dto.setFlavors(flavors);
        return dto;
    }

    /**
     * 修改菜品信息以及口味信息（两张表）
     * @param dishDto
     */
    @Override
    @Transactional // 添加事务注解，保证事务的一致性
    public void updateWithFlavor(DishDto dishDto) {
        // 修改dish基本信息
        this.updateById(dishDto);
        // 清理当前菜品对应的口味数据信息----dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        // 添加当前提交过来的口味数据信息----dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        // 批量保存
        dishFlavorService.saveBatch(flavors);
    }
}