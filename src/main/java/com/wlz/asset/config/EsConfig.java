package com.wlz.asset.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

@Configuration
public class EsConfig {

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private int port;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port, "http"));

        // 超时时间拉满，彻底解决超时
        builder.setRequestConfigCallback(config -> {
            config.setConnectTimeout(10000);
            config.setSocketTimeout(60000);
            config.setConnectionRequestTimeout(10000);
            return config;
        });

        return new RestHighLevelClient(builder);
    }

}
