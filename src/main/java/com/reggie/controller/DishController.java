package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.dto.DishDto;
import com.reggie.pojo.Dish;
import com.reggie.service.DishFlavorService;
import com.reggie.service.DishService;
import com.reggie.utis.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author： 陈晓松
 * @CLASS_NAME： DishController
 * @date： 2023/4/23 15:09
 * @注释：
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 菜品
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(name != null,Dish::getName,name);
        queryWrapper.orderByAsc(Dish::getUpdateTime);
        dishService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    /**
     * 新增菜品（双表）
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }





}