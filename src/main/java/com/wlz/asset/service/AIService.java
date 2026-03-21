package com.wlz.asset.service;

/**
 * AI服务统一接口
 */
public interface AIService {

    /**
     * 普通聊天调用
     * @param input 用户输入
     * @return AI响应
     */
    String chatCall(String input);

    /**
     * 自然语言转SQL调用
     * @param input 自然语言描述
     * @param systemPrompt 系统提示词
     * @return 生成的SQL语句
     */
    String generateSQL(String input, String systemPrompt);

}