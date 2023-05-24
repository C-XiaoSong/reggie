package com.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.pojo.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author： 陈晓松
 * @CLASS_NAME： AddressBookMapper
 * @date： 2023/5/12
 * @Description: TODO
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
