package com.reggie.utis;
/*
 * @author：陈晓松
 * @CLASS_NAME：BaseContext
 * @date：2023/4/21 11:33
 * @注释：基于ThreadLocal封装工具类，用户保存和获取当前登录用户的ID
 */

public class BaseContext { // 每次启动都是单独的线程，多次 请求存的值不会混乱
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }
    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
