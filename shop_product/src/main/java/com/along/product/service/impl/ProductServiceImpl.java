package com.along.product.service.impl;

import com.along.common.entity.Product;
import com.along.product.dao.ProductDao;
import com.along.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Desc
 * @Author wangtianlong
 * @Date 2024/3/15 17:17
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public Product findByPid(Integer pid) {
        return productDao.findById(pid).get();
    }

}
