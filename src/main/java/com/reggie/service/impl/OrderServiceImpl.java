package com.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.mapper.OrdersMapper;
import com.reggie.pojo.*;
import com.reggie.service.*;
import com.reggie.utis.BaseContext;
import com.reggie.utis.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author： 陈晓松
 * @CLASS_NAME： OrderServiceImpl
 * @date： 2023/5/24
 * @注释： 一共操作了三张表
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrderService {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private OrderDetailService orderDetailService;

    /**
    * @Author: 陈晓松
    * @Description: 用户下单
    * @Params: [order]
    * @Return void
    */
    @Override
    // @Transactional
    public void submit(Orders orders) {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        // 查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new CustomException("购物车为空，不能下单");
        }
        // 查询用户信息
        User user = userService.getById(userId);
        // 查询地址信息
        Long addressBookId = orders.getAddressBookId();// 地址ID
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null) {
            throw new CustomException("地址信息有误，不能下单");
        }
        // 生成订单号
        long orderId = IdWorker.getId();

        // 总金额，原子操作，可以保证多线程也不出错
        AtomicInteger atomic = new AtomicInteger(0);// 初始值：0

        // 获取金额，以及订单明细的信息
        List<OrderDetail> orderDetailList = shoppingCartList.stream().map(item -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);// 订单编号
            orderDetail.setNumber(item.getNumber());// 菜品或者套餐的数量
            orderDetail.setDishFlavor(item.getDishFlavor());// 对应的是口味
            orderDetail.setDishId(item.getDishId());// 菜品的ID
            orderDetail.setSetmealId(item.getSetmealId());// 套餐的ID
            orderDetail.setName(item.getName());// 菜品或者套餐的名称
            orderDetail.setImage(item.getImage());// 图片的名称
            orderDetail.setAmount(item.getAmount());// 当前的金额（单份的金额）
            // 进行累加，每次遍历之后从0进行累加
            // getAmount()：单份的金额
            // multiply；相当与乘号 X 、getNumber()；份数、intValue()；转成int值
            // 比如王老吉3块，两份就是 6块
            atomic.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        // 向订单表插入数据，一条数据
        orders.setOrderNumber(String.valueOf(orderId));// 设置订单号
        orders.setOrderTime(LocalDateTime.now());// 系统当前时间
        orders.setCheckoutTime(LocalDateTime.now());// 支付时间（不再开发真正的功能）
        orders.setStatus(2);// 状态码：2代表，待派送
        orders.setAmount(new BigDecimal(atomic.get()));// 订单总金额
        orders.setUserId(userId);// 用户ID
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());// 收货人
        orders.setPhone(addressBook.getPhone());// 手机号
        orders.setAddress(
                (addressBook.getProvinceName() == null ? "":addressBook.getProvinceName())+
                        (addressBook.getCityName() == null ? "":addressBook.getCityName())+
                        (addressBook.getDistrictName() == null ? "":addressBook.getDistrictName())+
                        (addressBook.getDetail() == null ? "":addressBook.getDetail())
        );// 设置拼接 省 市 县地址
        this.save(orders);
        // 向订单明细表插入数据，多条数据
        orderDetailService.saveBatch(orderDetailList);
        // 清空购物车数据
        shoppingCartService.remove(queryWrapper);
    }
}
