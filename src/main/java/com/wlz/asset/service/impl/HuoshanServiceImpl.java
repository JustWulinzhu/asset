package com.wlz.asset.service.impl;

import com.wlz.asset.dto.ColumnMetaDto;
import com.wlz.asset.dto.TableMetaDto;
import com.wlz.asset.mapper.DbMetadataMapper;
import com.wlz.asset.service.AIService;
import com.wlz.asset.service.AIServiceFactory;
import com.wlz.asset.service.HuoshanService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class HuoshanServiceImpl implements HuoshanService {

    @Resource
    private DbMetadataMapper dbMetadataMapper;

    @Resource
    private AIServiceFactory aiServiceFactory;

    @Override
    public String chatCall(String input) {
        AIService aiService = aiServiceFactory.getCurrentAIService();
        String response = aiService.chatCall(input);
        return response != null ? response : "Error: AI服务响应为空";
    }

    @Override
    public String nlToSql(String input) {
        // 1. 查询数据库表信息
        List<TableMetaDto> tables = dbMetadataMapper.getTables();
        if (CollectionUtils.isEmpty(tables)) {
            return "Error: 数据库表信息为空";
        }

        // 2. 遍历表，查询字段及类型
        StringBuilder metadataText = new StringBuilder();
        for (TableMetaDto table : tables) {
            String tableName = table.getTableName();
            String tableComment = table.getTableComment() == null || table.getTableComment().isEmpty()
                    ? tableName : table.getTableComment();

            metadataText.append("表名：").append(tableName).append("（").append(tableComment).append("）\n");
            metadataText.append("字段列表：\n");

            // 查询该表的字段
            List<ColumnMetaDto> columns = dbMetadataMapper.listColumns(tableName);
            for (ColumnMetaDto column : columns) {
                String columnName = column.getColumnName();
                String columnComment = column.getColumnComment() == null || column.getColumnComment().isEmpty()
                        ? columnName : column.getColumnComment();
                metadataText.append("- ").append(columnName).append("：").append(columnComment)
                        .append("（数据类型：").append(column.getDataType()).append("）\n");
            }
            metadataText.append("\n");
        }
        log.info("数据库元数据信息：{}", metadataText.toString());

        // 3. 构建系统Prompt
        String systemPrompt = buildSystemPrompt(metadataText.toString());

        // 4. 调用AI服务生成SQL
        AIService aiService = aiServiceFactory.getCurrentAIService();
        String sql = aiService.generateSQL(input, systemPrompt);

        // 5. sql校验，只能是SELECT语句，并且不能报错
        if (sql == null) {
            return "Error: AI服务未返回有效响应";
        }
        if (Objects.equals(sql, "无法生成合理的SQL")) {
            return sql;
        } else {
            if (!sql.toUpperCase().startsWith("SELECT")) {
                return "Error: 生成的SQL不是SELECT语句";
            } else {
                try {
                    // 这里可以添加SQL语法校验逻辑，确保生成的SQL语法正确
                    // 例如，可以使用第三方库进行SQL解析，或者简单地执行EXPLAIN来验证语法
                    // 执行explainSql，如果报错则说明SQL语法有问题
                    dbMetadataMapper.explainSql("EXPLAIN " + sql);
                } catch (Exception e) {
                    log.error("生成的SQL语法错误: {}", e.getMessage());
                    return "Error: 生成的SQL语法错误";
                }
            }
        }

        return sql;
    }

    /**
     * 构建生成SQL的Prompt
     *
     * @param metadata 数据库元数据
     * @return 生成SQL的Prompt
     */
    public String buildSystemPrompt(String metadata) {
        String systemPrompt = "你是一个专业的SQL生成助手，严格遵守以下规则生成SQL：\n" +
                "1. 仅使用提供的数据库元数据中的表和字段，禁止虚构表名/字段名；\n" +
                "2. 只生成SELECT语句，禁止生成DROP/DELETE/UPDATE/INSERT等修改数据的SQL；\n" +
                "3. SQL语法符合MySQL规范，字段名/表名用反引号`包裹，大表查询加LIMIT 100；\n" +
                "4. 只输出SQL语句，不添加任何解释、备注、语气词；\n" +
                "5. 如果问题无法转换为合理的SQL，请输出 '无法生成合理的SQL'；\n" +
                "\n数据库元数据：\n" + metadata;
        log.info("生成SQL的系统Prompt：{}", systemPrompt);
        return systemPrompt;
    }

    
}
