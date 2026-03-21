package com.wlz.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.wlz.asset.config.AIConfig;
import com.wlz.asset.dto.res.ai.AIChatResponse;
import com.wlz.asset.service.AIService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 智谱AI服务实现
 */
@Service
@Slf4j
public class ZhipuAIService implements AIService {

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
        List<Map<String, String>> messages = new ArrayList<>();

        // 系统提示词
        Map<String, String> system = new HashMap<>();
        system.put("role", "system");
        system.put("content", systemPrompt);
        messages.add(system);

        // 用户提示词
        Map<String, String> user = new HashMap<>();
        user.put("role", "user");
        user.put("content", input);
        messages.add(user);

        String response = callAI(input, messages);
        return response != null ? response.replaceAll("\\r?\\n", " ").trim() : null;
    }

    /**
     * 调用智谱API
     */
    private String callAI(String input, List<Map<String, String>> messages) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Bearer " + aiConfig.getZhipu().getApiKey());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", aiConfig.getZhipu().getModel());
        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, httpHeaders);

        try {
            ResponseEntity<AIChatResponse> response = restTemplate.postForEntity(
                aiConfig.getZhipu().getApiUrl(),
                requestEntity,
                AIChatResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                AIChatResponse responseBody = response.getBody();
                log.info("智谱API响应: {}", responseBody);

                // 提取返回内容
                StringBuilder sb = new StringBuilder();
                responseBody.getChoices().forEach(choice -> {
                    sb.append(choice.getMessage().getContent());
                });

                log.info("智谱API响应内容: {}", sb.toString());
                return sb.toString();
            } else {
                log.error("智谱API响应失败: {}", response.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            log.error("智谱API调用异常: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 构建默认消息列表
     */
    private List<Map<String, String>> buildDefaultMessages(String input) {
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> content = new HashMap<>();
        content.put("role", "system");
        content.put("content", input);
        messages.add(content);
        return messages;
    }
}