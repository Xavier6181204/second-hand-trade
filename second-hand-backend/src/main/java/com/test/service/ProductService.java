package com.test.service;

import com.test.entity.Product;
import java.util.List;

public interface ProductService {

    Product findById(Integer id);

    // 前台首页：只显示出售中
    List<Product> findList(Integer categoryId, String keyword);

    // 后台管理：显示所有
    List<Product> findAllList(Integer categoryId, String keyword);

    List<Product> findBySellerId(Integer sellerId);

    boolean addProduct(Product product);

    boolean updateProduct(Product product);

    boolean updateStatus(Integer id, Integer status);

    void viewProduct(Integer id);
}