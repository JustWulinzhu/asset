package com.wlz.asset.dto.res.huoshan;

import lombok.Data;
import java.util.List;

@Data
public class HuoshanChatResponse {

    // choices数组（核心，支持多个元素）
    private List<Choice> choices;
    // 其他字段
    private long created;
    private String id;
    private String model;
    private String service_tier;
    private String object;
    private Usage usage;

}
