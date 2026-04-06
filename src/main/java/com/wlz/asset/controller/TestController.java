package com.wlz.asset.controller;

import jakarta.annotation.Resource;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TestController {

    @Resource
    private RestHighLevelClient client;

    @RequestMapping("/test")
    public String test() throws IOException {
        GetRequest request = new GetRequest("phone", "S3KBYJ0BHtpQjVqoOpXR");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsString());
        return "Springboot test......";
    }

}
