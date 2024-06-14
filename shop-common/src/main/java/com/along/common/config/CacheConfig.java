package com.along.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @Desc Caffeine 缓存配置
 * @Author wangtianlong
 * @Date 2024/6/14
 */
@Configuration
public class CacheConfig {

    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("dataCache");
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .weakKeys()
                .recordStats();
    }
}