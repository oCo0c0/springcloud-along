package com.along.order.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * @Desc 发送消息
 * @Author wangtianlong
 * @Date 2024/5/28
 */
public class RocketMQSendTest {
    public static void main(String[] args) throws Exception {
        // 1. 创建消息生产者, 指定生产者所属的组名
        DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupName");
        // 2. 指定Nameserver地址
        producer.setNamesrvAddr("123.60.142.203:9876");
        // 3. 启动生产者
        producer.start();
        // 4. 创建消息对象，指定主题、标签和消息体
        Message msg = new Message("rocketmq-broker-1", "myTag",
                ("RocketMQ Message").getBytes());
        // 5. 发送消息
        SendResult sendResult = producer.send(msg, 10000);
        System.out.println(sendResult);
        // 6. 关闭生产者
        producer.shutdown();
    }
}
