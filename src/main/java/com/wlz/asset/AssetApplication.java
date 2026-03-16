package com.wlz.asset;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@MapperScan("com.wlz.asset.mapper") // 扫描Mapper接口
public class AssetApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssetApplication.class, args);
        log.info("AssetApplication启动成功 ᵔ◡ᵔ ᵔ◡ᵔ ᵔ◡ᵔ ᵔ◡ᵔ ᵔ◡ᵔ");
	}

}
