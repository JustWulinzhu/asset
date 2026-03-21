package com.wlz.asset.service.impl;

import com.wlz.asset.service.HuoshanService;
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
import org.springframework.beans.factory.annotation.Value;

@Service
@Slf4j
public class HuoshanServiceImpl implements HuoshanService {

    @Resource
    private RestTemplate restTemplate;

    @Value("${ai.huoshan.api.url}")
    private String huoshanApiUrl;

    @Value("${ai.huoshan.api.key}")
    private String huoshanApiKey;

    @Override
    public String chatCall(String input) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Bearer " + huoshanApiKey);

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "doubao-seed-2-0-pro-260215");

        Object[] messages = new Object[1];
        HashMap<String, String> content = new HashMap<>();
        content.put("role", "system");
        content.put("content", input);
        messages[0] = content;
        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(huoshanApiUrl, requestEntity, Map.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseBody = response.getBody();
                log.info("Huoshan API Response: {}", responseBody);
                // 提取核心返回内容
                Object choices = responseBody.get("choices");
                log.info("Huoshan API Response Choices: {}", choices);
                return ((Map)((Map)((Object[])choices)[0]).get("message")).get("content").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
