package com.wlz.asset.dto.res;

import lombok.Data;

@Data
public class UserRiskRes {

    private String userId;
    private Integer riskLevel;
    private String riskLevelName;
    private Integer riskScore;
    private String riskPreference;
    private String createTime;

}
