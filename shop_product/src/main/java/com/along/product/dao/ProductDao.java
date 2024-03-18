package com.along.product.dao;

import com.along.common.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Desc
 * @Author wangtianlong
 * @Date 2024/3/15 17:17
 */
@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {
}
