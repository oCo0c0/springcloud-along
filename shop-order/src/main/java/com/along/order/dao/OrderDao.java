package com.along.order.dao;

import com.along.common.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Desc
 * @Author wangtianlong
 * @Date 2024/3/15 17:40
 */
public interface OrderDao extends JpaRepository<Order,Long> {
}
