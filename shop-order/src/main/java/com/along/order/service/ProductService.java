package com.along.order.service;

import com.along.common.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Desc Feign创建一个接口实现其他服务接口的调用
 * @Author wangtianlong
 * @Date 2024/3/18 15:53
 */
@FeignClient("service-product") // 声明调用的提供者的name
public interface ProductService {

    // 指定调用提供者的哪个方法
    // @FeignClient+@GetMapping 就是一个完整的请求路径 http://service-product/product/{pid}
    @GetMapping(value = "/product/{pid}")
    Product findByPid(@PathVariable("pid") Integer pid);

}
