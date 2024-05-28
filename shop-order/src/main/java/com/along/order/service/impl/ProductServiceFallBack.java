package com.along.order.service.impl;

import com.along.common.entity.Product;
import com.along.order.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Desc 容错类要求必须实现被容错的接口,并为每个方法实现容错方案
 * @Author wangtianlong
 * @Date 2024/5/27
 */
@Component
@Slf4j
public class ProductServiceFallBack implements ProductService {

    @Override
    public Product findByPid(Integer pid) {
        Product product = new Product();
        product.setPid(-1);
        return product;
    }
}
