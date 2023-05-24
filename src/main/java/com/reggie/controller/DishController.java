package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.dto.DishDto;
import com.reggie.pojo.Category;
import com.reggie.pojo.Dish;
import com.reggie.pojo.DishFlavor;
import com.reggie.service.CategoryService;
import com.reggie.service.DishFlavorService;
import com.reggie.service.DishService;
import com.reggie.utis.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author： 陈晓松
 * @CLASS_NAME： DishController
 * @date： 2023/4/23 15:09
 * @注释： 菜品分类
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        Page<Dish> pageInfo = new Page<>(page, pageSize);

        // 连表查询需要两张表的属性下面dto继承了dish并且有category表中的字段
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(name != null,Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,queryWrapper);

        // 把pageInfo中的值 拷贝（复制）到dishDtoPage中，records不需要拷贝，应为records里面所有的值就是pageInfo所有的值
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map(item -> {
            DishDto dishDto = new DishDto();
            // 把item拷贝（复制）到dishDto上
            BeanUtils.copyProperties(item,dishDto); // 经过拷贝除了categoryName有了其他的属性值也有了

            // item就是遍历出每一个菜品对象
            Long categoryId = item.getCategoryId(); // 分类ID
            // 用分类ID去查分类表 把分类名称查出来（需要分类的Service）
            Category category = categoryService.getById(categoryId); // 根据ID查询出来的分类对象

            /*
            * 如果出错了就写个 if（category ！= null）
            * 把下面两行代码写进去
            * */

            // 获取category里面的分类名称Name值
            String categoryName = category.getName();
            // 需要给categoryName复制，需要new一个DishDto
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());

        // 进行赋值
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
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

    /**
     * 删除,批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        dishService.removeByIds(ids);
        return R.success("删除菜品成功");
    }

    /**
     * 根据ID查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        // 需要查两张表，在Service里面扩展一下
        DishDto dto = dishService.getByIdWithFlavor(id);
        return R.success(dto);
    }

    /**
     * 修改菜品信息以及口味信息
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品信息成功");
    }

    /**
     * 批量停售或起售
     * 修改菜品售卖信息（停售，起售 同一个方法）
     * @PathVariable("id") Long statusId：这是一个注解，表示将请求路径中的URL参数"id"值赋值给变量statusId
     * Request URL: http://localhost:8080/dish/status/0?ids=1653305586199752706,1653305531279536129
     * 例如，如果请求URL为/status/100?ids=1&ids=2&ids=3，那么statusId的值就会被赋为100。
     * @param statusId 接受过来的目前状态ID
     * @param ids 接受过来的菜品 ID
     * @return
     */
    @PostMapping("/status/{statusId}")
    public R<String> status(@PathVariable("statusId") Long statusId , @RequestParam Long[] ids){
        // Request URL: http://localhost:8080/dish/status/0?ids=1653305586199752706,1653305531279536129
        System.out.println("第一个："+statusId); // 对应的是0或者1 ，当起售时是 1，停售时是 0
        System.out.println("第二个："+ids); // 对应的是ids后面的值
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper();
        // in()方法指定了要更新的记录的id值必须为1、2或者3中的一个,因为ids 接受了对个传过来的ID
        // 将要修改状态的ID放进去
        updateWrapper.in(Dish::getId,ids);
        // 将要修改的值放进去 statusId
        updateWrapper.set(Dish::getStatus,statusId);
        // 调用修改修改方法
        dishService.update(updateWrapper);
        return R.success("状态修改成功");
    }
    
    /**
    * @Author: 陈晓松
    * @Description: 根据条件查询对应的菜品数据
    * @DateTime: 15:09 2023/5/4
    * @Params: [dish]
    * @Return com.reggie.utis.R<java.util.List<com.reggie.pojo.Dish>>
    */
    /*@GetMapping("/list")
    public R<List<Dish>> list (Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        // 添加条件，查询状态为1的（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);
    }*/

    @GetMapping("/list")
    public R<List<DishDto>> list (Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        // 添加条件，查询状态为1的（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dtoList = list.stream().map(item -> {
            DishDto dishDto = new DishDto();
            // 把item拷贝（复制）到dishDto上
            BeanUtils.copyProperties(item,dishDto); // 经过拷贝除了categoryName有了其他的属性值也有了

            // item就是遍历出每一个菜品对象
            Long categoryId = item.getCategoryId(); // 分类ID
            // 用分类ID去查分类表 把分类名称查出来（需要分类的Service）
            Category category = categoryService.getById(categoryId); // 根据ID查询出来的分类对象

            // 当前菜品的ID
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> flavorLambdaQueryWrapper = new LambdaQueryWrapper();
            flavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(flavorLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);

            // 获取category里面的分类名称Name值
            String categoryName = category.getName();
            // 需要给categoryName复制，需要new一个DishDto
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dtoList);
    }



}