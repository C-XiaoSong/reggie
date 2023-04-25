package com.reggie.service.impl;

/*
 * @author：陈晓松
 * @CLASS_NAME：SetmealServiceImpl
 * @date：2023/4/22 19:31
 * @注释：
 */

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.mapper.SetmealMapper;
import com.reggie.pojo.Setmeal;
import com.reggie.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
