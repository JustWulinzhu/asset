package com.wlz.asset.dto.res.ai;

import lombok.Data;
import java.util.List;

/**
 * 通用AI聊天响应DTO
 */
@Data
public class AIChatResponse {

    /**
     * 响应选项列表
     */
    private List<Choice> choices;

    /**
     * 创建时间戳
     */
    private long created;

    /**
     * 响应ID
     */
    private String id;

    /**
     * 模型名称
     */
    private String model;

    /**
     * 对象类型
     */
    private String object;

    /**
     * 使用量统计
     */
    private Usage usage;

    /**
     * 内部选择类
     */
    @Data
    public static class Choice {
        /**
         * 消息内容
         */
        private Message message;

        /**
         * 完成原因
         */
        private String finish_reason;

        /**
         * 索引
         */
        private int index;
    }

    /**
     * 内部消息类
     */
    @Data
    public static class Message {
        /**
         * 角色
         */
        private String role;

        /**
         * 内容
         */
        private String content;
    }

    /**
     * 使用量统计类
     */
    @Data
    public static class Usage {
        /**
         * 提示词token数量
         */
        private int prompt_tokens;

        /**
         * 完成token数量
         */
        private int completion_tokens;

        /**
         * 总token数量
         */
        private int total_tokens;

        /**
         * 提示词token详情
         */
        private PromptTokensDetails prompt_tokens_details;

        /**
         * 完成token详情
         */
        private CompletionTokensDetails completion_tokens_details;
    }

    /**
     * 提示词token详情
     */
    @Data
    public static class PromptTokensDetails {
        /**
         * 缓存token数量
         */
        private int cached_tokens;
    }

    /**
     * 完成token详情
     */
    @Data
    public static class CompletionTokensDetails {
        /**
         * 推理token数量
         */
        private int reasoning_tokens;

        /**
         * 接受token数量
         */
        private int accepted_tokens;

        /**
         * 拒绝token数量
         */
        private int rejected_tokens;
    }
}