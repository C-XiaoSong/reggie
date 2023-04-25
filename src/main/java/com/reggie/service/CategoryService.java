package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.pojo.Category;

/*
 * @author：陈晓松
 * @CLASS_NAME：CategoryService
 * @date：2023/4/21 8:59
 * @注释：
 */public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
