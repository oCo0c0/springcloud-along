package com.along.order.factory;

import com.along.common.entity.Product;
import com.along.order.service.ProductService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @Desc 容错工厂
 * @Author wangtianlong
 * @Date 2024/5/27
 */
@Component
public class ProductServiceFallBackFactory implements FallbackFactory<ProductService> {

    @Override
    public ProductService create(Throwable cause) {
        return pid -> {
            Product product = new Product();
            product.setPid(-1);
            return product;
        };
    }
}
