package com.wlz.asset.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Column;
import lombok.Data;

@Data
@TableName("user_risk_assessment")
public class UserRiskAssessment {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String userId;
    private Integer riskLevel;
    private Integer riskScore;
    private String riskPreference;
    private String createTime;


}
