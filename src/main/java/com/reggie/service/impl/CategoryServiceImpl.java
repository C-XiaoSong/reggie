package com.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.mapper.CategoryMapper;
import com.reggie.pojo.Category;
import com.reggie.pojo.Dish;
import com.reggie.pojo.Setmeal;
import com.reggie.service.CategoryService;
import com.reggie.service.DishService;
import com.reggie.service.SetmealService;
import com.reggie.utis.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * @author：陈晓松
 * @CLASS_NAME：CategoryServiceImpl
 * @date：2023/4/21 9:00
 * @注释：
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService; // 菜品
    @Autowired
    private SetmealService setmealService; // 套餐

    /**
     * 根据ID删除分类，删除前进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加条件，根据分类ID进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        // 查询当前分类是否关联了菜品，如果关联抛出一个异常
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1 > 0){
            // 自定义业务异常类
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加条件，根据分类ID进行查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        // 查询当前分类是否关联了套餐，如果关联抛出一个异常
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0){
            // 自定义业务异常类
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        /**
         * 1、super.removeById(id)是在子类中调用父类的removeById方法，而removeById(id)是在当前类中直接调用自身的removeById方法。
         * 2、super.removeById(id)可以重用父类中已经实现好的删除逻辑，而removeById(id)需要自行实现删除逻辑。
         * 正常删除分类
         */
        // super.removeById(id);
        removeById(id);
    }
}
