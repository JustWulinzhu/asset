package com.wlz.asset.service.impl;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Resource;
import com.wlz.asset.config.AIConfig;
import com.wlz.asset.service.AIService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.wlz.asset.dto.res.ai.AIChatResponse;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class DeepseekAIService implements AIService {

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

        if (response != null) {
            // 2. 用正则提取```sql和```之间的内容（大模型通常会用这个格式包裹SQL）
            Pattern pattern = Pattern.compile("```sql\\n(.*?)\\n```", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(response);
            if (matcher.find()) {
                return matcher.group(1).trim();
            }

            // 3. 若没有包裹，直接返回纯文本（兼容大模型的不同输出格式）
            return response.trim();
        }
        return null;
    }

    /**
     * 调用智谱API
     */
    private String callAI(String input, List<Map<String, String>> messages) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Bearer " + aiConfig.getDeepseek().getApiKey());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", aiConfig.getDeepseek().getModel());
        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, httpHeaders);

        try {
            ResponseEntity<AIChatResponse> response = restTemplate.postForEntity(
                    aiConfig.getDeepseek().getApiUrl(),
                    requestEntity,
                    AIChatResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                AIChatResponse responseBody = response.getBody();
                log.info("Deepseek API响应: {}", responseBody);

                // 提取返回内容
                StringBuilder sb = new StringBuilder();
                responseBody.getChoices().forEach(choice -> {
                    sb.append(choice.getMessage().getContent());
                });

                log.info("Deepseek API响应内容: {}", sb.toString());
                return sb.toString();
            } else {
                log.error("Deepseek API响应失败: {}", response.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            log.error("Deepseek API调用异常: {}", e.getMessage(), e);
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
