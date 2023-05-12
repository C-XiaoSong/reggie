package com.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.mapper.UserMapper;
import com.reggie.pojo.User;
import com.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author： 陈晓松
 * @CLASS_NAME： UserServiceImpl
 * @date： 2023/5/10
 * @注释：
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
