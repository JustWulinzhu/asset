package com.wlz.asset.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice // 核心注解：标记这是全局异常处理器，作用于所有@RestController接口，
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> handleValidException(MethodArgumentNotValidException e) {
        // 打印异常日志（包含堆栈，方便后端排查）
        log.error("参数校验异常 >> ", e);
        // 拼接字段错误提示（比如：phone：手机号格式不正确；name：客户名称不能为空；）
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMsg = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMsg.append(fieldError.getField()) // 错误字段名
                    .append("：")
                    .append(fieldError.getDefaultMessage()) // 错误提示语
                    .append("；");
        }
        // 返回400（参数错误）+ 拼接的提示语
        return R.error(HttpStatus.BAD_REQUEST.value(), errorMsg.toString());
    }

    // 兜底处理：捕获所有未定义的异常（系统异常）
    @ExceptionHandler(Exception.class)
    public R<?> handleException(Exception e) {
        log.error("全局异常捕获异常 >> ", e);
        // 系统异常只返回通用提示，不暴露底层报错（比如数据库连接失败、SQL错误）
        return R.error(500, "系统繁忙，请稍后重试");
    }

}
