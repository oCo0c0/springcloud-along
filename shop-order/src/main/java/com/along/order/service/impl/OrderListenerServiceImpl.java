package com.along.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.along.common.dao.TxLogDao;
import com.along.common.entity.Order;
import com.along.common.entity.TxLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @Desc
 * @Author wangtianlong
 * @Date 2024/5/29
 */
@Slf4j
@RocketMQTransactionListener()
public class OrderListenerServiceImpl implements RocketMQLocalTransactionListener {

    @Resource
    private TxLogDao txLogDao;
    @Resource
    private OrderRocketServiceImpl orderRocketService;

    // 执行本地事物
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try {
            // 本地事物
            orderRocketService.createOrder((String) msg.getHeaders().get("txId"),
                    (Order) arg);
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    //消息回查
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        // 查询日志记录
        TxLog txLog = new TxLog();
        Long id = (Long) msg.getHeaders().get("txId");
        if (null != id) {
            Optional<TxLog> optionalTxLog = txLogDao.findById(id);
            if (optionalTxLog.isPresent()) {
                txLog = optionalTxLog.get();
            }
        }
        log.info("事务日志打印：{}", JSON.toJSONString(txLog));
        return RocketMQLocalTransactionState.ROLLBACK;
    }

}
