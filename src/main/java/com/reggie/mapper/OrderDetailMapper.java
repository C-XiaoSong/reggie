package com.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.pojo.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author： 陈晓松
 * @CLASS_NAME： OrderDetailMapper
 * @date： 2023/5/24
 * @Description: TODO
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
