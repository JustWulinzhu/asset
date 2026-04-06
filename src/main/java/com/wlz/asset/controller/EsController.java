package com.wlz.asset.controller;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import com.wlz.asset.common.R;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.constraints.NotBlank;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/es")
@Slf4j
public class EsController {

    @Resource
    private RestHighLevelClient client;

    @RequestMapping("/query/{searchParams}")
    public R<?> query(@PathVariable @NotBlank(message = "搜索参数不能为空") String searchParams) {
        try {

            // 1. 创建搜索请求，指定要查询的索引名称
            SearchRequest request = new SearchRequest("phone");

            // 2. 构造搜索源对象（相当于外层的 { "query": {...} }）
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            // 3. 添加 match 查询（相当于 "match": { "name": "小米" }）
            searchSourceBuilder.query(QueryBuilders.matchQuery("name", searchParams));

            // 4. 将搜素条件放入请求中
            request.source(searchSourceBuilder);

            // 5. 执行查询，拿到响应结果
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            log.info("原生查询结果：{}", response);

            // 6. 解析结果
            List<Map<String, Object>> results = new ArrayList<>();
            response.getHits().forEach(hit -> {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                log.info("遍历查询结果map：{}", sourceAsMap);
                results.add(sourceAsMap);
            });

            return R.success(results);
        } catch (Exception e) {
            log.error("查询ES数据失败", e);
            return R.error("查询ES数据失败");
        }
    }

}
