package com.wlz.asset.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * AI服务配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ai")
public class AIConfig {

    /**
     * 当前启用的AI服务提供商
     */
    private String provider = "huoshan";

    /**
     * 火山引擎API配置
     */
    private Huoshan huoshan = new Huoshan();

    /**
     * 智谱API配置
     */
    private Zhipu zhipu = new Zhipu();

    /**
     * Deepseek API配置
     */
    private Deepseek deepseek = new Deepseek();

    /**
     * Deepseek API配置
     */
    private Gemini gemini = new Gemini();

    @Data
    public static class Huoshan {
        /**
         * API地址
         */
        private String apiUrl;

        /**
         * API密钥
         */
        private String apiKey;

        /**
         * 模型名称
         */
        private String model = "doubao-seed-2-0-pro-260215";

    }

    @Data
    public static class Zhipu {
        /**
         * API地址
         */
        private String apiUrl;

        /**
         * API密钥
         */
        private String apiKey;

        /**
         * 模型名称
         */
        private String model = "glm-4.7";

    }

    @Data
    public static class Deepseek {

        /**
         * API地址
         */
        private String apiUrl;

        /**
         * API密钥
         */
        private String apiKey;

        /**
         * 模型名称
         */
        private String model = "deepseek-reasoner";

    }

    @Data
    public static class Gemini {
        /**
         * API地址
         */
        private String apiUrl;

        /**
         * API密钥
         */
        private String apiKey;

        /**
         * 模型名称
         */
        private String model = "gemini-3-flash-preview";

    }

}