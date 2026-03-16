package com.wlz.asset.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wlz.asset.entity.UserDeepKyc;

public interface UserDeepKycMapper extends BaseMapper<UserDeepKyc> {

    UserDeepKyc getKycByUserId(String userId);

}
