package com.along.product.dao;

import com.along.common.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Desc
 * @Author wangtianlong
 * @Date 2024/3/15 17:17
 */
public interface ProductDao extends JpaRepository<Product, Integer> {
}
