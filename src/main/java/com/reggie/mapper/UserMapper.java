package com.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author： 陈晓松
 * @CLASS_NAME： UserMapper
 * @date： 2023/5/10
 * @Description: TODO
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
