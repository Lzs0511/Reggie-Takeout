package com.kpsilent.reggie.common;

/**
 * 基于ThreadLocal封装一个工具类，用来保存和获取当前登录的ID
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();

    public static  void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
