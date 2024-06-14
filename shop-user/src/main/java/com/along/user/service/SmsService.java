package com.along.user.service;

import com.alibaba.fastjson.JSON;
import com.along.common.entity.Order;
import com.along.common.entity.User;
import com.along.user.dao.UserDao;
import com.along.user.sms.SmsUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @Desc 发送短信的服务
 * @Author wangtianlong
 * @Date 2024/5/29
 */
@Slf4j
@Service
@RocketMQMessageListener(
        consumerGroup = "shop-user", //消费者组名
        topic = "order-topic",//消费主题
        consumeMode = ConsumeMode.CONCURRENTLY,//消费模式
        messageModel = MessageModel.CLUSTERING//消息模式
)
public class SmsService implements RocketMQListener<Order> {

    @Resource
    private UserDao userDao;

    @Override
    public void onMessage(Order order) {
        log.info("收到一个订单信息{},接下来发送短信", JSON.toJSONString(order));

        //根据uid 获取手机号
        User user = userDao.findById(Long.valueOf(order.getUid())).get();
        // 生成验证码
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            builder.append(new Random().nextInt(9) + 1);
        }
        String smsCode = builder.toString();
        Param param = new Param(smsCode);
        try {
            // 发送短信 {"code":"123456"}
            SmsUtil.sendSms(user.getTelephone(), "springcloud-sms", "SMS_170836451", JSON.toJSONString(param));
            log.info("短信发送成功");
        } catch (Exception e) {
            log.error("异常信息打印：{}", e.getMessage());
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static
    class Param {
        private String code;
    }

}
