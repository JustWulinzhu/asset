package com.wlz.asset.service.impl;

import com.wlz.asset.common.enums.RiskLevelEnum;
import com.wlz.asset.dto.res.UserRiskRes;
import com.wlz.asset.entity.UserRiskAssessment;
import com.wlz.asset.mapper.UserRiskAssessmentMapper;
import com.wlz.asset.service.UserRiskAssessmentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserRiskAssessmentImpl implements UserRiskAssessmentService {

    @Resource
    private UserRiskAssessmentMapper userRiskAssessmentMapper;

    @Override
    public UserRiskRes getRiskByUserId(String userId) {
        UserRiskAssessment ret = userRiskAssessmentMapper.getRiskByUserId(userId);

        if (ret != null) {
            UserRiskRes res = new UserRiskRes();
            res.setUserId(ret.getUserId());
            res.setRiskLevel(ret.getRiskLevel());
            res.setRiskLevelName(RiskLevelEnum.getRiskNameByLevel(ret.getRiskLevel()));
            res.setRiskScore(ret.getRiskScore());
            res.setRiskPreference(ret.getRiskPreference());
            res.setCreateTime(ret.getCreateTime());
            return res;
        }
        return null;
    }

}
