package com.wlz.asset.controller;

import com.wlz.asset.common.R;
import com.wlz.asset.dto.res.RiskAssessmentRes;
import com.wlz.asset.dto.res.UserKycRes;
import com.wlz.asset.dto.res.UserRiskRes;
import com.wlz.asset.service.UserDeepKycService;
import com.wlz.asset.service.UserRiskAssessmentService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/asset")
public class AssetController {

    @Resource
    private UserRiskAssessmentService userRiskAssessmentService;

    @Resource
    private UserDeepKycService userDeepKycService;

    /**
     * 获取用户风险评估结果
     *
     * @param userId 用户ID
     * @return 评估结果
     */
    @GetMapping("/risk/{userId}")
    public R<RiskAssessmentRes> getRiskAssessment(@PathVariable String userId) {
        log.info("获取用户{}的资产评估结果", userId);
        UserRiskRes retRisk = userRiskAssessmentService.getRiskByUserId(userId);
        UserKycRes retKyc = userDeepKycService.getKycByUserId(userId);
        log.info("用户{}的风险评估结果为：{}", userId, retRisk);
        log.info("用户{}的深度KYC结果为：{}", userId, retKyc);

        RiskAssessmentRes ret = new RiskAssessmentRes();
        ret.setUserRiskRes(retRisk);
        ret.setUserKycRes(retKyc);
        return R.success(ret);
    }

}
