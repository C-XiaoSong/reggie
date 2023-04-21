package com.reggie.utis;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/*
 * @author：陈晓松
 * @CLASS_NAME：MyMetaObjectHandler
 * @date：2023/4/21 9:46
 * @注释：添加或者修改的时候自动填充的字段
 *
 */

/**
 * 自定义原数据对象处理器
 *
 * @Component 用于声明一个类是 Spring 框架中的一个组件。
 * 组件是指在应用程序中可以被重用的对象，通常是一个类。
 * 这个注解告诉 Spring 框架将这个类实例化并将其注册为一个 bean，以便在应用程序中可以使用它。
 *
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 公共字段自动填充（插入）
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
    /**
     * 公共字段自动填充（插入或修改）
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}
