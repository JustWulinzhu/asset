package com.wlz.asset.controller;

import com.wlz.asset.common.R;
import com.wlz.asset.service.HuoshanService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/huoshan")
public class HuoshanController {

    @Resource
    private HuoshanService huoshanService;

    /**
     * 获取用户风险评估结果
     *
     * @param input 用户输入
     * @return 评估结果
     */
    @PostMapping("/chat")
    public R<String> getRiskAssessment(@RequestBody String input) {
        String result = huoshanService.chatCall(input);
        return R.success(result);
    }

}

