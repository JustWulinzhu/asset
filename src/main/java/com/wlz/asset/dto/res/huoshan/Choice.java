package com.wlz.asset.dto.res.huoshan;

import lombok.Data;

@Data
public class Choice {

    private String finish_reason;
    private int index;
    private Object logprobs; // 通常为null，用Object接收即可
    private Message message;

}
