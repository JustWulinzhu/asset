package com.wlz.asset.service;

import com.wlz.asset.dto.res.UserRiskRes;
import com.wlz.asset.entity.UserRiskAssessment;

public interface UserRiskAssessmentService {

    UserRiskRes getRiskByUserId(String userId);

}
