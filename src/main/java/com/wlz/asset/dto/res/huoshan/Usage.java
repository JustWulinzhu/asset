package com.wlz.asset.dto.res.huoshan;

import lombok.Data;

@Data
public class Usage {

    private int completion_tokens;
    private int prompt_tokens;
    private int total_tokens;
    private PromptTokensDetails prompt_tokens_details;
    private CompletionTokensDetails completion_tokens_details;

}
