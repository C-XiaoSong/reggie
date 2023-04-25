package com.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.mapper.DishFlavorMapper;
import com.reggie.pojo.DishFlavor;
import com.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
 * @author： 陈晓松
 * @CLASS_NAME： DishFlavorServiceImpl
 * @date： 2023/4/23 15:24
 * @注释：
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
