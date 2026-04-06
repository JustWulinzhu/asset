package com.wlz.asset.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import com.alibaba.fastjson.JSON;
import com.wlz.asset.common.R;
import com.wlz.asset.dto.req.EsPhonedReq;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.constraints.NotBlank;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/es")
@Slf4j
public class EsController {

    @Resource
    private RestHighLevelClient client;

    @RequestMapping("/query-by-id/{id}")
    public R<?> queryById(@PathVariable String id) {
        try {
            GetRequest getRequest = new GetRequest("phone", id);
            GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
            log.info("查询结果1：{}", response);
            if (response.isExists()) {
                Map<String, Object> sourceAsMap = response.getSourceAsMap();
                log.info("查询结果2：{}", sourceAsMap);
                return R.success(sourceAsMap);
            } else {
                return R.error("未查询到数据");
            }
        } catch (Exception e) {
            log.error("查询ES数据失败", e);
            return R.error("查询ES数据失败");
        }
    }

    @RequestMapping("/query/{searchParams}")
    public R<?> query(@PathVariable @NotBlank(message = "搜索参数不能为空") String searchParams) {
        try {

            // 1. 创建搜索请求，指定要查询的索引名称
            SearchRequest request = new SearchRequest("phone");

            // 2. 构造搜索源对象（相当于外层的 { "query": {...} }）
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            // 3. 添加 match 查询（相当于 "match": { "name": "小米" }）
            searchSourceBuilder.query(QueryBuilders.matchQuery("name", searchParams));

            //  添加排序条件
            searchSourceBuilder.sort("price", SortOrder.DESC);

            // 4. 将搜素条件放入请求中
            request.source(searchSourceBuilder);

            // 5. 执行查询，拿到响应结果
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            log.info("原生查询结果：{}", response);

            // 6. 解析结果
            List<Map<String, Object>> results = new ArrayList<>();
            response.getHits().forEach(hit -> {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                results.add(sourceAsMap);
            });
            log.info("接口返回：{}", results);

            return R.success(results);
        } catch (Exception e) {
            log.error("查询ES数据失败", e);
            return R.error("查询ES数据失败");
        }
    }

    /**
     * 搜索接口，支持多条件组合查询
     * @param req 查询参数对象，包含name、brand、minPrice、maxPrice等字段
     * @return 搜索结果
     */
    @RequestMapping("/search")
    public R<?> query2(@RequestBody EsPhonedReq req) {
        try {
            // 1. 创建搜索请求，指定要查询的索引名称
            SearchRequest request = new SearchRequest("phone");

            // 2. 构造搜索源对象（相当于外层的 { "query": {...} }）
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            // 3. 构建 bool 复合查询
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.matchQuery("name", req.getName()));
            boolQueryBuilder.must(QueryBuilders.termQuery("brand", req.getBrand()));
            boolQueryBuilder.must(QueryBuilders.rangeQuery("price").lte(req.getMaxPrice()).gte(req.getMinPrice()));

            // 3. 把查询条件放入 SearchSourceBuilder
            searchSourceBuilder.query(boolQueryBuilder);

            // 4. 添加排序条件
            searchSourceBuilder.sort("price", SortOrder.DESC);

            // 5. 将搜索条件放入请求中
            request.source(searchSourceBuilder);

            // 6. 执行查询，拿到响应结果
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            log.info("原生查询结果：{}", response);

            // 6. 解析结果
            List<Map<String, Object>> results = new ArrayList<>();
            response.getHits().forEach(hit -> {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                results.add(sourceAsMap);
            });
            log.info("接口返回：{}", results);

            return R.success(results);
        } catch (Exception e) {
            log.error("查询ES数据失败", e);
            return R.error("查询ES数据失败");
        }
    }

    /**
     * 添加数据到ES
     * @param req
     * @return
     */
    @RequestMapping("/add")
    public R add(@RequestBody EsPhonedReq req) {
        try {
            log.info("添加数据接口参数：{}", req);
            // 1、创建请求
            IndexRequest request = new IndexRequest("phone");

            // 2、设置文档ID（可选，如果不设置，ES会自动生成一个唯一ID）
            if (req.getId() != null) {
                request.id(req.getId());
            }

            // 3、将数据转json 塞入请求中
            Map<String, Object> reqMap = new HashMap<>();
            reqMap.put("name", req.getName());
            reqMap.put("price", req.getPrice());
            reqMap.put("brand", req.getBrand());
            String reqJson = JSON.toJSONString(reqMap);
            request.source(reqJson, XContentType.JSON);

            // 4、执行请求
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            log.info("添加数据接口返回：{}", response);

            return R.success();
        } catch (Exception e) {
            log.error("查询ES数据失败", e);
            return R.error("查询ES数据失败");
        }
    }

}
