package com.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.pojo.Category;
import org.apache.ibatis.annotations.Mapper;

/*
 * @author：陈晓松
 * @CLASS_NAME：CategoryMapper
 * @date：2023/4/21 9:01
 * @注释：
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
