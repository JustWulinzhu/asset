package com.wlz.asset.dto.res.huoshan;

import lombok.Data;

@Data
public class Message {

    private String content; // 模型返回的核心内容
    private String role;    // 角色（assistant）

}
