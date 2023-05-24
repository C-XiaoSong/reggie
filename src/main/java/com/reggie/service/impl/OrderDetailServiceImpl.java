package com.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.mapper.OrderDetailMapper;
import com.reggie.pojo.OrderDetail;
import com.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author： 陈晓松
 * @CLASS_NAME： OrderDetailServiceImpl
 * @date： 2023/5/24
 * @注释： TODO
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
