package com.along.order.dao;

import com.along.common.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Desc
 * @Author wangtianlong
 * @Date 2024/3/15 17:40
 */
@Repository
public interface OrderDao extends JpaRepository<Order,Long> {
}
