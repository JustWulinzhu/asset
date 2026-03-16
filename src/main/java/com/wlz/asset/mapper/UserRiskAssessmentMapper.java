package com.wlz.asset.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wlz.asset.entity.UserRiskAssessment;

public interface UserRiskAssessmentMapper extends BaseMapper<UserRiskAssessment> {

    UserRiskAssessment getRiskByUserId(String userId);

}
