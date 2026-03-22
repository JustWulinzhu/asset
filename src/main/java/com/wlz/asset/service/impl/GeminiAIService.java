package com.wlz.asset.service.impl;

import com.alibaba.fastjson2.JSON;
import com.wlz.asset.config.AIConfig;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class GeminiAIService implements AIService {

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
     * 调用谷歌geminiAPI
     */
    private String callAI(String input, List<Map<String, String>> messages) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("x-goog-api-key", aiConfig.getGemini().getApiKey());

        Map<String, Object> requestBody = new HashMap<>();
        // contents 是核心：包含角色（user）和提问内容
        List<Map<String, Object>> contents = List.of(
                Map.of(
                        "role", "user",
                        "parts", List.of(Map.of("text", input))
                )
        );
        requestBody.put("contents", contents);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, httpHeaders);

        try {
            String url = aiConfig.getGemini().getApiUrl() + "/" + aiConfig.getGemini().getModel() + ":generateContent";
            log.info("Gemini API请求URL: {}", url);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    url,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String responseBody = response.getBody();
                log.info("Gemini API响应: {}", responseBody);
                String result = parseGeminiResponse(responseBody);
                log.info("Gemini API format响应: {}", result);
                return result;
            } else {
                log.error("Gemini API响应失败: {}", response.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            log.error("Gemini API调用异常: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 解析 Gemini 响应体，提取核心文本
     * @param responseBody Gemini 返回的 JSON 字符串
     * @return 生成的文本内容
     */
    private String parseGeminiResponse(String responseBody) {
        try {
            // 用 fastjson2 解析 JSON（需引入依赖：com.alibaba.fastjson2:fastjson2:2.0.42）
            Map<String, Object> responseMap = JSON.parseObject(responseBody);
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseMap.get("candidates");
            if (candidates == null || candidates.isEmpty()) {
                return "Gemini 未返回有效内容";
            }
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            return parts.get(0).get("text").toString();
        } catch (Exception e) {
            throw new RuntimeException("解析 Gemini 响应失败：" + e.getMessage(), e);
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
