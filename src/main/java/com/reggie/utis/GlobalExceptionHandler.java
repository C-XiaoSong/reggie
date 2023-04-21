package com.reggie.utis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/*
 * @author：陈晓松
 * @CLASS_NAME：GlobalExceptionHandler
 * @date：2023/4/19 18:57
 * @注释：全局异常处理（新增员工账号重复处理）
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class}) // 只要加了这两个都会被处理
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 异常处理方法
     * @ExceptionHandler(Exception.class) 表示该方法将处理所有类型的异常。
     * 如果希望处理特定类型的异常，可以将 Exception.class 替换为其他异常类。
     * @ExceptionHandler 注解只能用于 Controller 类中的方法上，不能用于其他类或方法上
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class) // sql语句唯一键重复异常
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException e){
        log.info(e.getMessage());
        // contains包含
        if (e.getMessage().contains("Duplicate entry")){
/*
            String[] split = e.getMessage().split(" ");
            String msg = split[2]+"已存在";
            return R.error(msg);

            */
            return R.error("该账号已存在");
        }
        return R.error("未知错误");
    }
}
