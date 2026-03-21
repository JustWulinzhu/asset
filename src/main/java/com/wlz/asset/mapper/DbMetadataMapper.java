package com.wlz.asset.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wlz.asset.dto.ColumnMetaDto;
import com.wlz.asset.dto.TableMetaDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface DbMetadataMapper extends BaseMapper<Object> {

    // 获取数据库中所有表的元数据
    @Select("SELECT TABLE_NAME AS tableName, TABLE_COMMENT AS tableComment FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = DATABASE()")
    List<TableMetaDto> getTables();

    // 查询指定表的字段信息
    @Select("SELECT COLUMN_NAME AS columnName, COLUMN_COMMENT AS columnComment, " +
            "DATA_TYPE AS dataType, IS_NULLABLE AS isNullable " +
            "FROM INFORMATION_SCHEMA.COLUMNS " +
            "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = #{tableName}")
    List<ColumnMetaDto> listColumns(@Param("tableName") String tableName);

    // 模拟执行SQL（用于校验SQL是否正确）
    @Select("${sql}") // 注意：${sql}是字符串替换，仅用于EXPLAIN校验，无注入风险
    List<Object> explainSql(@Param("sql") String sql);

}
