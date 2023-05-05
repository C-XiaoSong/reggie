package com.reggie.service.impl;

/*
 * @author：陈晓松
 * @CLASS_NAME：SetmealServiceImpl
 * @date：2023/4/22 19:31
 * @注释：
 */

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.dto.SetmealDto;
import com.reggie.mapper.SetmealMapper;
import com.reggie.pojo.Setmeal;
import com.reggie.pojo.SetmealDish;
import com.reggie.service.SetmealDishService;
import com.reggie.service.SetmealService;
import com.reggie.utis.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * @Author: 陈晓松
     * @Description: 新增套餐，同时关联菜品和套餐关联的关系
     * @DateTime: 15:26 2023/5/4
     * @Params: [setmealDto]
     * @Return void
     */
    @Transactional // 事务注解，保证数据的一致性
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存套餐的基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map(item ->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        // 保存套餐和菜品的关联信息，操作setmeal_dish，执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
    * @Author: 陈晓松
    * @Description: 删除套餐，同时删除套餐和菜品关联的数据
    * @DateTime: 16:31 2023/5/4
    * @Params: [ids]
    * @Return void
    */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        // 查询套餐状态是否可以删除（起售状态不能删除）
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Setmeal::getId,ids); // 条件查询getId是否等于ids接受过来的值
        queryWrapper.eq(Setmeal::getStatus,1); // 等值查询，getStatus是否为 1

        int count = this.count(queryWrapper); // 框架的方法
        if (count > 0 ){
            // 如果不能删除就抛出一个异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        // 如果可以删除，先删除套餐表中的数据---setmeal
        this.removeByIds(ids);
        // 删除关系表中的数据---setmeal_dish
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper);
    }
}
