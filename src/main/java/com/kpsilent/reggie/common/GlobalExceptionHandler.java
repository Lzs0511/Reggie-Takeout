package com.kpsilent.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@RestController
@Slf4j
public class GlobalExceptionHandler {
    // 处理哪个异常
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception){
        // 日志打印错误信息
        log.error(exception.getMessage());
        // 如果错误信息中包含Duplicate entry 返回信息：账户名已存在
        if(exception.getMessage().contains("Duplicate entry")){
            String[] s = exception.getMessage().split(" ");
            String msg = s[2] + "已存在";
            return R.error(msg);
        }
        return R.success("未知错误");
    }
}
