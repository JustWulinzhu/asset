package com.wlz.asset.dto.res;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UserKycRes {

    private String userId;
    private Integer age;
    private BigDecimal income;
    private BigDecimal assets;
    private BigDecimal debt;
    private String investmentExperience;
    private String investmentGoal;
    private String liquidityDemand;

}
