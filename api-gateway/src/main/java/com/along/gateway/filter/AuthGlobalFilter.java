package com.along.gateway.filter;

import com.alibaba.nacos.client.utils.StringUtils;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Desc 全局过滤器 实现GlobalFilter和Ordered接口
 * @Author wangtianlong
 * @Date 2024/3/21 14:00
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    // 完成判断逻辑
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain
            chain) {
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        if (StringUtils.isBlank(token)) {
            System.out.println("鉴权失败");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        // 调用chain.filter继续向下游执行
        return chain.filter(exchange);
    }

    // 顺序,数值越小,优先级越高
    @Override
    public int getOrder() {
        return 0;
    }
}
