package com.wlz.asset.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.alibaba.fastjson.JSON;
import com.wlz.asset.common.R;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import com.wlz.asset.dto.req.EsPhonedReq;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/elasticsearch")
@Slf4j
public class ElasticsearchController {

    @Resource
    private ElasticsearchClient esClient;

    @RequestMapping("/add")
    public R add(@RequestBody EsPhonedReq req) {
        try {

            return R.success();
        } catch (Exception e) {
            log.error("添加ES数据失败", e);
            return R.error("添加ES数据失败");
        }
    }

}
