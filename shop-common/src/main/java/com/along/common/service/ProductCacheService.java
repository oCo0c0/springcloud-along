package com.along.common.service;

import com.along.common.entity.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Function;

/**
 * @Desc 数据预热，缓存
 * @Author wangtianlong
 * @Date 2024/6/14
 */
@Service
public class ProductCacheService {

    @Resource
    private CacheManager cacheManager;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final BlockingQueue<String> cacheDeletionQueue = new LinkedBlockingDeque<>();

    public CompletableFuture<List<Product>> getProductInfo(String productId) {
        return CompletableFuture.completedFuture(Collections.singletonList(getCacheProduct(productId)));
    }

    private Product getCacheProduct(String productId) {
        return getCacheData("productCache", productId, Product.class, this::queryProduct);
    }

    public <T> T getCacheData(String cacheName, String key, Class<T> type, Function<String, T> daQuery) {
        // 尝试从 Caffeine 缓存获取
        Cache caffeineCache = cacheManager.getCache(cacheName);
        T value = null;
        if (caffeineCache != null) {
            value = caffeineCache.get(key, type);
        }

        if (value == null) {
            String cachedData = stringRedisTemplate.opsForValue().get(key);
            if (cachedData != null) {
                value = convertStringToObject(cachedData, type);
            } else {
                value = daQuery.apply(key);
                stringRedisTemplate.opsForValue().set(key, convertObjectToString(value));
            }
            if (caffeineCache != null) {
                caffeineCache.put(key, value);
            }
        }
        return value;
    }

    private <T> T convertStringToObject(String data, Class<T> type) {
        // 假设使用 Jackson 来转换 JSON 字符串到对象
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(data, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert string to object", e);
        }
    }

    private <T> String convertObjectToString(T object) {
        // 假设使用 Jackson 来转换对象到 JSON 字符串
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to string", e);
        }
    }

    private Product queryProduct(String s) {
        return new Product();
    }


    public void updateProduct(Product product) {
        // 删除缓存
        // 更新数据库
        // 添加延时删除队列
        cacheDeletionQueue.add(String.valueOf(product.getPid()));
    }

    public void delayedCacheDeletion() {
        String productId = cacheDeletionQueue.poll();
        if (productId != null) {
            stringRedisTemplate.delete(productId);
        }
    }

}
