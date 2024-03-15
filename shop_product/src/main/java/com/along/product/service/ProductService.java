package com.along.product.service;

import com.along.common.entity.Product;

/**
 * @Desc
 * @Author wangtianlong
 * @Date 2024/3/15 17:19
 */
public interface ProductService {

    Product findByPid(Integer pid);

}
