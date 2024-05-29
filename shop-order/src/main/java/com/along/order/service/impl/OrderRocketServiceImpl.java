package com.along.order.service.impl;

import com.along.common.dao.TxLogDao;
import com.along.common.entity.Order;
import com.along.common.entity.TxLog;
import com.along.order.dao.OrderDao;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

/**
 * @Desc
 * @Author wangtianlong
 * @Date 2024/5/29
 */
@Service
public class OrderRocketServiceImpl {

    @Resource
    private OrderDao orderDao;

    @Resource
    private TxLogDao txLogDao;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void createOrderBefore(Order order) {
        String txId = UUID.randomUUID().toString();

        // 发送半事务消息
        rocketMQTemplate.sendMessageInTransaction("tx_producer_group",
                MessageBuilder.withPayload(order).setHeader("txId", txId).build(), order);

    }

    // 本地事物
    @Transactional
    public void createOrder(String txId, Order order) {
        // 本地事物代码
        orderDao.save(order);
        // 记录日志到数据库,回查使用
        TxLog txLog = new TxLog();
        txLog.setTxLogId(txId);
        txLog.setContent("事物测试");
        txLog.setDate(new Date());
        txLogDao.save(txLog);
    }
}

