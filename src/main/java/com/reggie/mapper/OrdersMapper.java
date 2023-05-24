package com.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author： 陈晓松
 * @CLASS_NAME： OrdersMapper
 * @date： 2023/5/24
 * @Description: TODO
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
