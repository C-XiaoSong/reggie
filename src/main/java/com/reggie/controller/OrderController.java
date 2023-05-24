package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.pojo.Orders;
import com.reggie.service.OrderService;
import com.reggie.utis.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author： 陈晓松
 * @CLASS_NAME： OrderController
 * @date： 2023/5/24
 * @注释： TODO
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
    * @Author: 陈晓松
    * @Description: 用户下单
    * @Params: [order]
    * @Return com.reggie.utis.R<com.reggie.pojo.Order>
    */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        orderService.submit(orders);
        return R.success("下单成功");
    }

    /**
    * @Author: 陈晓松
    * @Description: 查看订单
    * @Params: [page, pageSize]
    * @Return com.reggie.utis.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
    */
    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize) {
        Page<Orders> pageInfo = new Page(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Orders::getOrderTime);
        orderService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
}
