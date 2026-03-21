package com.wlz.asset.controller;

import com.wlz.asset.config.AIConfig;
import com.wlz.asset.service.AIService;
import com.wlz.asset.service.AIServiceFactory;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI配置控制器
 * 用于测试和切换AI服务
 */
@RestController
@RequestMapping("/api/ai/config")
public class AIConfigController {

    @Resource
    private AIConfig aiConfig;

    @Resource
    private AIServiceFactory aiServiceFactory;

    /**
     * 获取当前配置
     */
    @GetMapping("/current")
    public String getCurrentConfig() {
        return String.format("当前AI服务提供商: %s", aiConfig.getProvider());
    }

    /**
     * 获取火山引擎配置
     */
    @GetMapping("/huoshan")
    public String getHuoshanConfig() {
        return String.format("火山引擎API地址: %s, 模型: %s",
            aiConfig.getHuoshan().getApiUrl(),
            aiConfig.getHuoshan().getModel());
    }

    /**
     * 获取智谱配置
     */
    @GetMapping("/zhipu")
    public String getZhipuConfig() {
        return String.format("智谱API地址: %s, 模型: %s",
            aiConfig.getZhipu().getApiUrl(),
            aiConfig.getZhipu().getModel());
    }

    /**
     * 测试AI服务调用
     */
    @GetMapping("/test")
    public String testAIService(@RequestParam(required = false) String provider) {
        AIService aiService;
        if (provider != null) {
            aiService = aiServiceFactory.getAIService(provider);
        } else {
            aiService = aiServiceFactory.getCurrentAIService();
        }

        String testInput = "你好，请回复'服务正常'";
        String response = aiService.chatCall(testInput);

        return String.format("AI服务测试结果 - 服务商: %s, 响应: %s",
            provider != null ? provider : aiConfig.getProvider(),
            response != null ? response : "无响应");
    }
}