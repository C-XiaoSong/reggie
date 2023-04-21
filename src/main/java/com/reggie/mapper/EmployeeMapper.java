package com.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

/*
 * @author：陈晓松
 * @CLASS_NAME：EmployeeMapper
 * @date：2023/4/18 19:02
 * @注释：
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
