package com.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.pojo.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author： 陈晓松
 * @CLASS_NAME： ShoppingCartMapper
 * @date： 2023/5/12
 * @Description: TODO
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
