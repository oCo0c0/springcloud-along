package com.along.order.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Desc 订单Sentinel实现类 限流和降级方法定义在方法中
 * @Author wangtianlong
 * @Date 2024/5/27
 */

@Service
@Slf4j
public class OrderSentinelService1Impl {

    int i = 0;

    @SentinelResource(
            value = "message",
            blockHandler = "blockHandler", // 指定发生BlockException时进入的方法
            fallback = "fallback" // 指定发生Throwable时进入的方法
    )
    public String message() {
        i++;
        if (i % 3 == 0) {
            throw new RuntimeException();
        }
        return "message";
    }

    //BlockException时进入的方法
    public String blockHandler(BlockException ex) {
        log.error("{}", ex);
        return "接口被限流或者降级了...";
    }

    //Throwable时进入的方法
    public String fallback(Throwable throwable) {
        log.error("{}", throwable);
        return "接口发生异常了...";
    }

}