package com.wlz.asset.service.impl;

import com.wlz.asset.dto.res.UserKycRes;
import com.wlz.asset.entity.UserDeepKyc;
import com.wlz.asset.mapper.UserDeepKycMapper;
import com.wlz.asset.service.UserDeepKycService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDeepKycServiceImpl implements UserDeepKycService {

    @Resource
    private UserDeepKycMapper userDeepKycMapper;

    @Override
    public UserKycRes getKycByUserId(String userId) {
        UserDeepKyc userDeepKyc = userDeepKycMapper.getKycByUserId(userId);
        if (userDeepKyc != null) {
            UserKycRes res = new UserKycRes();
            log.info("查询到用户{}的深度KYC信息：{}", userId, userDeepKyc);
            BeanUtils.copyProperties(userDeepKyc, res);
            log.info("查询到用户{}的深度KYC信息2：{}", userId, res);
            return res;
        }
        return null;
    }

}
