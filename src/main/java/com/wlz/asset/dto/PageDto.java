package com.wlz.asset.dto;

import lombok.Data;
import java.util.List;

/**
 * 自定义分页响应DTO（无records层级，无冗余参数）
 * 主要作用就是自定义分页的响应参数，避免返回一些MyBatis-Plus的冗余的参数
 * 更贴近实际工作情况
 */
@Data
public class PageDto<T> {

    /** 分页列表数据（直接暴露，替代原records） */
    private List<T> list;
    /** 总记录数（保留） */
    private Long total;
    /** 每页条数（保留） */
    private Long pageSize;
    /** 总页数（保留） */
    private Long pages;

    // 从MyBatis-Plus的IPage转换为自定义DTO的工具方法
    // 主要作用就是自定义分页的响应参数，避免返回一些MyBatis-Plus的冗余的参数
    public static <T> PageDto<T> fromIPage(com.baomidou.mybatisplus.core.metadata.IPage<T> iPage) {
        PageDto<T> dto = new PageDto<>();
        // 把IPage的records直接赋值给list，删除records层级
        dto.setList(iPage.getRecords());
        dto.setTotal(iPage.getTotal());
        dto.setPageSize(iPage.getSize());
        dto.setPages(iPage.getPages());
        return dto;
    }

    /**
     * T是 泛型，“Type” 的缩写，代表 “任意引用类型”（比如 User、Order、Product 等业务实体类）
     * 它不是具体的类型，而是一个 “占位符”—— 在使用这个类时，才会确定T的具体类型。
     * 可以理解成 “类型占位符”
     */

}