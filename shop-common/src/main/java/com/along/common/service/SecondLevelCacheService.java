package com.along.common.service;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Desc 二级缓存实现
 * @Author wangtianlong
 * @Date 2024/6/14
 */
@Service
public class SecondLevelCacheService {

    @Resource
    private CacheManager cacheManager;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public Object getData(String key) {
        // 尝试从Caffeine 缓存获取
        Cache caffeineCache = cacheManager.getCache("dataCache");
        Object value = caffeineCache.get(key);

        if (null == value) {
            // 尝试从 redis 缓存获取
            value = stringRedisTemplate.opsForValue().get(key);
            if (null == value) {
                // 缓存未命中，从数据库查询
                value = loadDataFromDB(key);
                stringRedisTemplate.opsForValue().set(key, value.toString());
            }
            caffeineCache.put(key, value);
        }
        return value;
    }

    private Object loadDataFromDB(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

}
