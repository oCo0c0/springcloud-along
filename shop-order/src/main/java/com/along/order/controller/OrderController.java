package com.along.order.controller;

import com.alibaba.fastjson.JSON;
import com.along.common.entity.Order;
import com.along.common.entity.Product;
import com.along.order.service.OrderService;
import com.along.order.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

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
    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private ProductService productService;
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Value("${spring.profiles.active}")
    private String active;

    // 准备买1件商品
    @GetMapping("/order/prod/{pid}")
    public Order order(@PathVariable("pid") Integer pid) {
        log.info(">>客户下单，这时候要调用商品微服务查询商品信息");
        // 通过restTemplate调用商品微服务
        Product product = restTemplate.getForObject("http://127.0.0.1:8081/product/" + pid, Product.class);
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

    // nacos服务注册
    @GetMapping("/order/nacos-prod/{pid}")
    public Order getOrderByNacos(@PathVariable("pid") Integer pid) {
        log.info(">>客户下单，这时候要调用商品微服务查询商品信息");
        // 从nacos中获取服务地址
        ServiceInstance serviceInstance =
                discoveryClient.getInstances("service-product-" + active).get(0);
        String url = serviceInstance.getHost() + ":" +
                serviceInstance.getPort();
        log.info(">>从nacos中获取到的微服务地址为:" + url);
        // 通过restTemplate调用商品微服务
        Product product = restTemplate.getForObject(
                "http://" + url + "/product/" + pid, Product.class);
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

    // 自定义负载均衡
    @GetMapping("/order/loadBalancing-prod/{pid}")
    public Order getOrderByloadBalancing(@PathVariable("pid") Integer pid) {
        log.info(">>客户下单，这时候要调用商品微服务查询商品信息");
        // 从nacos中获取服务地址
        // 自定义规则实现随机挑选服务
        List<ServiceInstance> instances = discoveryClient.getInstances("serviceproduct");
        int index = new Random().nextInt(instances.size());
        ServiceInstance serviceInstance = instances.get(index);
        String url = serviceInstance.getHost() + ":" +
                serviceInstance.getPort();
        log.info(">>从nacos中获取到的微服务地址为:" + url);
        // 通过restTemplate调用商品微服务
        Product product = restTemplate.getForObject("http://" + url +
                "/product/" + pid, Product.class);
        log.info(">>商品信息，查询结果:" + JSON.toJSONString(product));
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

    @GetMapping("/order/ribbon-prod/{pid}")
    public Order getOrderByRibbon(@PathVariable("pid") Integer pid) {
        log.info(">>客户下单，这时候要调用商品微服务查询商品信息");
        // 直接使用微服务名字， 从nacos中获取服务地址
        String url = "service-product-" + active;
        // 通过restTemplate调用商品微服务
        Product product = restTemplate.getForObject(
                "http://" + url + "/product/" + pid, Product.class);
        log.info(">>商品信息，查询结果:" + JSON.toJSONString(product));
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

    // feign调用
    @GetMapping("/order/feign-prod/{pid}")
    public Order getOrderByFeign(@PathVariable("pid") Integer pid) {
        log.info(">>客户下单,这时候要调用商品微服务查询商品信息");
        // 通过fegin调用商品微服务
        Product product = productService.findByPid(pid);
        log.info(">>商品信息,查询结果:" + JSON.toJSONString(product));
        Order order = new Order();
        order.setUid(1);
        order.setUsername("测试用户");
        order.setPid(product.getPid());
        order.setPname(product.getPname());
        order.setPprice(product.getPprice());
        order.setNumber(1);
        orderService.save(order);
        // 下单成功之后,将消息放到mq中
        rocketMQTemplate.convertAndSend("order-topic", order);
        return order;
    }

    @RequestMapping("/order/fallback-prod/{pid}")
    public Order getOrderByFallBack(@PathVariable("pid") Integer pid) {
        log.info("接收到{}号商品的下单请求,接下来调用商品微服务查询此商品信息", pid);
        // 调用商品微服务,查询商品信息
        Product product = productService.findByPid(pid);
        if (product.getPid() == -1) {
            Order order = new Order();
            order.setPname("下单失败");
            return order;
        }
        log.info("查询到{}号商品的信息,内容是:{}", pid, JSON.toJSONString(product));
        // 下单(创建订单)
        Order order = new Order();
        order.setUid(1);
        order.setUsername("测试用户");
        order.setPid(pid);
        order.setPname(product.getPname());
        order.setPprice(product.getPprice());
        order.setNumber(1);
        orderService.save(order);
        log.info("创建订单成功,订单信息为{}", JSON.toJSONString(order));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            log.error("异常打印：{}", e.getMessage());
        }
        return order;
    }

}
