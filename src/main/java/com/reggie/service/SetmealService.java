package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.dto.SetmealDto;
import com.reggie.pojo.Setmeal;

import java.util.List;

/*
 * @author：陈晓松
 * @CLASS_NAME：SetmealService
 * @date：2023/4/22 19:28
 * @注释：
 */
public interface SetmealService extends IService<Setmeal> {
    /**
    * @Author: 陈晓松
    * @Description: 新增套餐，同时关联菜品和套餐关联的关系
    * @DateTime: 15:26 2023/5/4
    * @Params: [setmealDto]
    * @Return void
    */
    void saveWithDish(SetmealDto setmealDto);
    
    /**
    * @Author: 陈晓松
    * @Description: 删除套餐，同时删除套餐和菜品关联的数据
    * @DateTime: 16:31 2023/5/4
    * @Params: [ids]
    * @Return void
    */
    void removeWithDish(List<Long> ids);
}
