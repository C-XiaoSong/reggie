package com.reggie.utis;

/*
 * @author：陈晓松
 * @CLASS_NAME：CustomException
 * @date：2023/4/22 20:00
 * @注释：自定义业务异常类
 */

/**
 * RuntimeException：运行时异常
 */
public class CustomException extends RuntimeException {
    public CustomException(String message){
        super(message);
    }
}
