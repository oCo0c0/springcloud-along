package com.along.order.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Desc 订单Sentinel实现类 限流和降级方法外置到单独的类中
 * @Author wangtianlong
 * @Date 2024/5/27
 */
@Service
@Slf4j
public class OrderSentinelService2Impl {
    int i = 0;
    @SentinelResource(
            value = "message",
            blockHandlerClass = OrderServiceImpl3BlockHandlerClass.class,
            blockHandler = "blockHandler",
            fallbackClass = OrderServiceImpl3FallbackClass.class,
            fallback = "fallback"
    )
    public String message() {
        i++;
        if (i % 3 == 0) {
            throw new RuntimeException();
        }
        return "message4";
    }

    @Slf4j
    public static class OrderServiceImpl3BlockHandlerClass {
        //注意这里必须使用static修饰方法
        public static String blockHandler(BlockException ex) {
            log.error("{}", ex);
            return "接口被限流或者降级了...";
        }
    }

    @Slf4j
    public static class OrderServiceImpl3FallbackClass {
        //注意这里必须使用static修饰方法
        public static String fallback(Throwable throwable) {
            log.error("{}", throwable);
            return "接口发生异常了...";
        }
    }
}

