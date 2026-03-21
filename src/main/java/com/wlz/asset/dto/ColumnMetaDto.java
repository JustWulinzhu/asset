package com.wlz.asset.dto;

import lombok.Data;

@Data
public class ColumnMetaDto {

    private String columnName;    // 字段名
    private String columnComment; // 字段注释
    private String dataType;      // 数据类型
    private String isNullable;    // 是否可为空

}
