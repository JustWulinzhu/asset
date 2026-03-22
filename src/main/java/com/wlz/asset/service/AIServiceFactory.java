package com.wlz.asset.service;

import com.wlz.asset.config.AIConfig;
import com.wlz.asset.service.impl.DeepseekAIService;
import com.wlz.asset.service.impl.HuoshanAIService;
import com.wlz.asset.service.impl.ZhipuAIService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * AI服务工厂
 * 根据配置选择具体的AI服务实现
 */
@Component
public class AIServiceFactory {

    @Resource
    private AIConfig aiConfig;

    @Resource
    private HuoshanAIService huoshanAIService;

    @Resource
    private ZhipuAIService zhipuAIService;

    @Resource
    private DeepseekAIService deepseekAIService;

    /**
     * 获取当前配置的AI服务
     * @return AI服务实例
     */
    public AIService getCurrentAIService() {
        return switch (aiConfig.getProvider().toLowerCase()) {
            case "huoshan" -> huoshanAIService;
            case "zhipu" -> zhipuAIService;
            case "deepseek" -> deepseekAIService;
            default -> {
                log.warn("未知的AI服务提供商: {}, 默认使用火山引擎", aiConfig.getProvider());
                yield huoshanAIService;
            }
        };
    }

    /**
     * 获取指定提供商的AI服务
     * @param provider 提供商名称 (huoshan 或 zhipu)
     * @return AI服务实例
     */
    public AIService getAIService(String provider) {
        return switch (provider.toLowerCase()) {
            case "huoshan" -> huoshanAIService;
            case "zhipu" -> zhipuAIService;
            case "deepseek" -> deepseekAIService;
            default -> throw new IllegalArgumentException("不支持的AI服务提供商: " + provider);
        };
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AIServiceFactory.class);
}