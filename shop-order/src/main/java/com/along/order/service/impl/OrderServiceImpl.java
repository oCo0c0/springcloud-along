package com.along.order.service.impl;

import com.along.common.entity.Order;
import com.along.order.dao.OrderDao;
import com.along.order.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Desc
 * @Author wangtianlong
 * @Date 2024/3/15 17:41
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Override
    public void save(Order order) {
        orderDao.save(order);
    }

}
