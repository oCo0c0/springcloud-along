package com.along.order.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Desc
 * @Author wangtianlong
 * @Date 2024/3/21 11:36
 */
@RestController
@RefreshScope
public class NacosConfigController {
    @Resource
    private ConfigurableApplicationContext applicationContext;

    @GetMapping("/nacos-config-test")
    public String nacosConfingTest1() {
        return applicationContext.getEnvironment().getProperty("config.appName");
    }

    @Value("${config.env}")
    private String env;
    //3 同一微服务的不同环境下共享配置
    @GetMapping("/nacos-config-test1")
    public String nacosConfingTest3() {
        return env;
    }
}
