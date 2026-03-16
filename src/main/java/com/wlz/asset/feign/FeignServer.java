package com.wlz.asset.feign;

import com.wlz.asset.common.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "FeignServer", url = "http://localhost:8880")
public interface FeignServer {

    @GetMapping("/user/list")
    public R<List> getUserList();

}
