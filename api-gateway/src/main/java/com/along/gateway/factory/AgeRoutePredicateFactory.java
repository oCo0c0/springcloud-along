package com.along.gateway.factory;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @Desc 断言工厂
 * @Author wangtianlong
 * @Date 2024/3/21 13:37
 */
@Component
public class AgeRoutePredicateFactory extends AbstractRoutePredicateFactory<AgeRoutePredicateFactory.Config> { //泛型 用于接收一个配置类,配置类用于接收中配置文件中的配置

    public AgeRoutePredicateFactory() {
        super(AgeRoutePredicateFactory.Config.class);
    }

    // 用于从配置文件中获取参数值赋值到配置类中的属性上
    @Override
    public List<String> shortcutFieldOrder() {
        // 这里的顺序要跟配置文件中的参数顺序一致
        return Arrays.asList("minAge", "maxAge");
    }

    // 断言
    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return serverWebExchange -> {
            // 从serverWebExchange获取传入的参数
            String ageStr = serverWebExchange.getRequest().getQueryParams().getFirst("age");
            if (StringUtils.isNotEmpty(ageStr)) {
                int age = Integer.parseInt(ageStr);
                return age >= config.getMinAge() && age <= config.getMaxAge();
            }
            return false;
        };
    }

    // 自定义一个配置类, 用于接收配置文件中的参数
    @Data
    static class Config {
        private int minAge;
        private int maxAge;
    }

}
