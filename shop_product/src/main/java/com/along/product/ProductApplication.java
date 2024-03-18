package com.along.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Desc
 * @Author wangtianlong
 * @Date 2024/3/15 17:12
 */
@SpringBootApplication
@EnableDiscoveryClient
@EntityScan(basePackages = "com.along.common.entity")
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

}
