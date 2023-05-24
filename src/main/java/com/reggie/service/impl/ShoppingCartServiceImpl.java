package com.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.mapper.ShoppingCartMapper;
import com.reggie.pojo.ShoppingCart;
import com.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author： 陈晓松
 * @CLASS_NAME： ShoppingCartServiceImpl
 * @date： 2023/5/12
 * @注释： TODO
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
