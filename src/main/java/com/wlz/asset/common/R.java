package com.wlz.asset.common;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 全局统一响应工具类
 * @param <T> 响应数据类型（支持任意类型：List、对象、String等）
 */
@Data // Lombok自动生成get/set/toString，简化代码
public class R<T> implements Serializable {

    // 1. 业务状态码：默认200（成功），其他值表示失败
    private Integer code = 200;

    // 2. 提示消息：默认"操作成功"，失败时自定义
    private String msg = "操作成功";

    // 3. 业务数据：接口实际返回的内容，无数据时为null
    private T data;

    // 4. 响应时间戳：自动填充当前时间，无需手动设置
    private LocalDateTime timestamp = LocalDateTime.now();

    // 私有构造器：禁止外部直接new，通过静态工具方法创建（保证字段默认值）
    private R() {}

    // -------------------------- 成功响应工具方法 --------------------------
    /**
     * 成功响应（无业务数据，如新增/删除接口）
     */
    public static <T> R<T> success() {
        return new R<>(); // 直接用默认值：code=200，msg=操作成功，data=null
    }

    /**
     * 成功响应（带业务数据，如查询接口）
     * @param data 业务数据（List/对象/String等）
     */
    public static <T> R<T> success(T data) {
        R<T> r = new R<>();
        r.setData(data); // 仅设置数据，code/msg用默认值
        return r;
    }

    /**
     * 成功响应（自定义提示语+数据，如特殊成功场景）
     * @param msg 自定义提示语
     * @param data 业务数据
     */
    public static <T> R<T> success(String msg, T data) {
        R<T> r = new R<>();
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    // -------------------------- 失败响应工具方法 --------------------------
    /**
     * 失败响应（自定义状态码+提示语，无数据）
     * @param code 业务失败码（如400=参数错误，500=系统异常，5001=客户不存在）
     * @param msg  失败提示语
     */
    public static <T> R<T> error(Integer code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(null); // 失败时数据固定为null
        return r;
    }

    /**
     * 失败响应（默认状态码500+自定义提示语，最常用）
     * @param msg 失败提示语
     */
    public static <T> R<T> error(String msg) {
        return error(500, msg); // 复用上面的方法，code默认500
    }

    /**
     * 失败响应（默认状态码500+默认提示语：系统异常，请稍后重试）
     */
    public static <T> R<T> error() {
        return error(500, "系统异常，请稍后重试");
    }

}