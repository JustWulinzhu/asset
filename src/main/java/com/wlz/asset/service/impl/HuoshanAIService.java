package com.wlz.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.wlz.asset.config.AIConfig;
import com.wlz.asset.dto.res.huoshan.HuoshanChatResponse;
import com.wlz.asset.service.AIService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 火山引擎AI服务实现
 */
@Service
@Slf4j
public class HuoshanAIService implements AIService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private AIConfig aiConfig;

    @Override
    public String chatCall(String input) {
        return callAI(input, buildDefaultMessages(input));
    }

    @Override
    public String generateSQL(String input, String systemPrompt) {
        Object[] messages = new Object[2];

        // 系统提示词
        HashMap<String, String> system = new HashMap<>();
        system.put("role", "system");
        system.put("content", systemPrompt);
        messages[0] = system;

        // 用户提示词
        HashMap<String, String> user = new HashMap<>();
        user.put("role", "user");
        user.put("content", input);
        messages[1] = user;

        String response = callAI(input, messages);
        return response != null ? response.replaceAll("\\r?\\n", " ").trim() : null;
    }

    /**
     * 调用火山引擎API
     */
    private String callAI(String input, Object[] messages) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Bearer " + aiConfig.getHuoshan().getApiKey());

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", aiConfig.getHuoshan().getModel());
        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, httpHeaders);

        try {
            ResponseEntity<HuoshanChatResponse> response = restTemplate.postForEntity(
                aiConfig.getHuoshan().getApiUrl(),
                requestEntity,
                HuoshanChatResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                HuoshanChatResponse responseBody = response.getBody();
                log.info("火山引擎API响应: {}", responseBody);

                // 提取返回内容
                StringBuilder sb = new StringBuilder();
                responseBody.getChoices().forEach(choice -> {
                    sb.append(choice.getMessage().getContent());
                });

                log.info("火山引擎API响应内容: {}", sb.toString());
                return sb.toString();
            } else {
                log.error("火山引擎API响应失败: {}", response.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            log.error("火山引擎API调用异常: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 构建默认消息数组
     */
    private Object[] buildDefaultMessages(String input) {
        Object[] messages = new Object[1];
        HashMap<String, String> content = new HashMap<>();
        content.put("role", "system");
        content.put("content", input);
        messages[0] = content;
        return messages;
    }
}