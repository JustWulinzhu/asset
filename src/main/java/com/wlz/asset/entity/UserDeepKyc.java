package com.wlz.asset.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("user_deep_kyc")
public class UserDeepKyc {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String userId;

    private Integer age;

    private BigDecimal income;

    private BigDecimal assets;

    private BigDecimal debt;

    private String investmentExperience;

    private String investmentGoal;

    private String liquidityDemand;

}