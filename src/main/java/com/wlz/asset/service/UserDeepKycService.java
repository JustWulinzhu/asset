package com.wlz.asset.service;

import com.wlz.asset.dto.res.UserKycRes;

public interface UserDeepKycService {

    UserKycRes getKycByUserId(String userId);

}
