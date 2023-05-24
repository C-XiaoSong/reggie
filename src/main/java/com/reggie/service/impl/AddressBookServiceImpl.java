package com.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.mapper.AddressBookMapper;
import com.reggie.pojo.AddressBook;
import com.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author： 陈晓松
 * @CLASS_NAME： AddressBookServiceImpl
 * @date： 2023/5/12
 * @注释： TODO
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
