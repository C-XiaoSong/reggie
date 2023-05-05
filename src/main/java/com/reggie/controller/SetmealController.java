package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.dto.SetmealDto;
import com.reggie.pojo.Category;
import com.reggie.pojo.Setmeal;
import com.reggie.service.CategoryService;
import com.reggie.service.SetmealDishService;
import com.reggie.service.SetmealService;
import com.reggie.utis.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author： 陈晓松
 * @CLASS_NAME： SetmealController
 * @date： 2023/5/4 9:47
 * @注释： 套餐管理
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealDishService setmealDishService;

    /**
    * @Author: 陈晓松
    * @Description: 分页查询（两张表）
    * @Params: [page, pageSize ,name]
    * @Return com.reggie.utis.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
    */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize , String name){

        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null , Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo,queryWrapper);
        // 对象拷贝,records类型不一样，不需要拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            // 对象拷贝
            BeanUtils.copyProperties(item,setmealDto);
            // 分类id
            Long categoryId = item.getCategoryId();
            // 根据分类ID查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null){
                // 分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;

        }).collect(Collectors.toList());
        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }

    /**
    * @Author: 陈晓松
    * @Description: 新增套餐
    * @Params: [setmealDto]
    * @Return com.reggie.utis.R<java.lang.String>
    */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    /**
    * @Author: 陈晓松
    * @Description: 批量删除，如果正在启售提示删除失败
    * @Params: [ids]
    * @Return com.reggie.utis.R<java.lang.String>
    */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return R.success("删除成功");
    }

    /**
    * @Author: 陈晓松
    * @Description: 批量（停售/起售）
    * @Params: [statusId, ids]
    * @Return com.reggie.utis.R<java.lang.String>
    */
    @PostMapping("/status/{statusId}")
    public R<String> status(@PathVariable("statusId") int statusId , @RequestParam List<Long> ids){
        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Setmeal::getId,ids);
        updateWrapper.set(Setmeal::getStatus,statusId);
        setmealService.update(updateWrapper);
        return R.success("修改状态成功");
    }



}
