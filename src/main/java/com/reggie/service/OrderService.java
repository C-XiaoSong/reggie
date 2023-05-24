package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.pojo.Orders;

/**
 * @author： 陈晓松
 * @CLASS_NAME： OrdersService
 * @date： 2023/5/24
 * @Description: TODO
 */
public interface OrderService extends IService<Orders> {

    /**
    * @Author: 陈晓松
    * @Description: 用户下单
    * @Params: [order]
    * @Return void
    */
    void submit(Orders order);
}
