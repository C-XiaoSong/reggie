package com.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.mapper.CategoryMapper;
import com.reggie.pojo.Category;
import com.reggie.service.CategoryService;
import org.springframework.stereotype.Service;

/*
 * @author：陈晓松
 * @CLASS_NAME：CategoryServiceImpl
 * @date：2023/4/21 9:00
 * @注释：
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
