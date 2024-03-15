package com.along.order.controller;

import com.alibaba.fastjson.JSON;
import com.along.common.entity.Order;
import com.along.common.entity.Product;
import com.along.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @Desc
 * @Author wangtianlong
 * @Date 2024/3/15 17:51
 */
@RestController
@Slf4j
public class OrderController {

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private OrderService orderService;

    // 准备买1件商品
    @GetMapping("/order/prod/{pid}")
    public Order order(@PathVariable("pid") Integer pid) {
        log.info(">>客户下单，这时候要调用商品微服务查询商品信息");
        // 通过restTemplate调用商品微服务
        Product product = restTemplate.getForObject("http://localhost:8081/product/" + pid, Product.class);
        log.info(">>商品信息,查询结果:" + JSON.toJSONString(product));
        Order order = new Order();
        order.setUid(1);
        order.setUsername("测试用户");
        order.setPid(product.getPid());
        order.setPname(product.getPname());
        order.setPprice(product.getPprice());
        order.setNumber(1);
        orderService.save(order);
        return order;
    }

}
