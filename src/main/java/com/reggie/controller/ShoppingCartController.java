package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.reggie.pojo.ShoppingCart;
import com.reggie.service.ShoppingCartService;
import com.reggie.utis.BaseContext;
import com.reggie.utis.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author： 陈晓松
 * @CLASS_NAME： ShoppingCartController
 * @date： 2023/5/12
 * @注释： 购物车功能
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
    * @Author: 陈晓松
    * @Description: 查看购物车
    * @Params: [shoppingCart]
    * @Return com.reggie.utis.R<java.util.List<com.reggie.pojo.ShoppingCart>>
    */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper();
        // 根据userId查询
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        // 最后加进去的菜品放在最上面展示
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    /**
    * @Author: 陈晓松
    * @Description: 添加到购物车的信息
    * @Params: [shoppingCart]
    * @Return com.reggie.utis.R<java.lang.String>
    */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        // 设置用户ID、登录成功之后可以用这种方式获取用户ID：BaseContext.getCurrentId()
        // 设置当前是那个用户的购物车
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        // 查询当前菜品或者套餐是否已存在购物车
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        if (dishId != null){
            // 添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            // 添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        // 查询当前菜品或者套餐是否在购物车中
        ShoppingCart carServiceOne = shoppingCartService.getOne(queryWrapper);
        if (carServiceOne != null){
            // 如果存在，就在原来的数量基础上加一
            Integer number = carServiceOne.getNumber();
            carServiceOne.setNumber(number + 1);
            // 更新
            shoppingCartService.updateById(carServiceOne);
        }else {
            // 如果不存在就默认数量是一并添加到购物车
            shoppingCart.setNumber(1);
            // 设置添加的时间
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            carServiceOne = shoppingCart;
        }
        return R.success(carServiceOne);
    }

    /**
    * @Author: 陈晓松
    * @Description: 购物车数量减少
    * @Params: [shoppingCart]
    * @Return com.reggie.utis.R<java.lang.String>
    */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        // 设置用户ID、登录成功之后可以用这种方式获取用户ID：BaseContext.getCurrentId()
        // 设置当前是那个用户的购物车
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        // 查询当前菜品或者套餐是否已存在购物车
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        if (dishId != null){
            // 添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            // 添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        // 查询当前菜品或者套餐是否在购物车中
        ShoppingCart carServiceOne = shoppingCartService.getOne(queryWrapper);
        // 如果存在，就在原来的数量基础上减一
        Integer number = carServiceOne.getNumber();
        // 如果购物车数量是 1 并且用户点击了减号，那么直接删除这个菜
        if (number == 1){
            Long id = carServiceOne.getId();
            shoppingCartService.removeById(id);
        }else {
            // 如果不是 1 就在原数上减一
            carServiceOne.setNumber(number - 1);
        }
        // 根据ID更新
        shoppingCartService.updateById(carServiceOne);
        carServiceOne = shoppingCart;
        return R.success(carServiceOne);
    }

    /**
    * @Author: 陈晓松
    * @Description: 清除购物车
    * @Params: [request]
    * @Return com.reggie.utis.R<java.lang.String>
    */
    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("已清空购物车");
    }
}
