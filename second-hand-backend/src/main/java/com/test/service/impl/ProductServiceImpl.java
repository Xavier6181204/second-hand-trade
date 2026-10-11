package com.test.service.impl;

import com.test.entity.Product;
import com.test.mapper.ProductMapper;
import com.test.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Product findById(Integer id) {
        return productMapper.findById(id);
    }

    @Override
    public List<Product> findList(Integer categoryId, String keyword) {
        return productMapper.findList(categoryId, keyword, 1);
    }

    @Override
    public List<Product> findAllList(Integer categoryId, String keyword) {
        return productMapper.findAllList(categoryId, keyword);
    }

    @Override
    public List<Product> findBySellerId(Integer sellerId) {
        return productMapper.findBySellerId(sellerId);
    }

    @Override
    @Transactional
    public boolean addProduct(Product product) {
        product.setStatus(0);
        product.setViewCount(0);
        return productMapper.insert(product) > 0;
    }

    @Override
    public boolean updateProduct(Product product) {
        return productMapper.update(product) > 0;
    }

    @Override
    @Transactional
    public boolean updateStatus(Integer id, Integer status) {
        return productMapper.updateStatus(id, status) > 0;
    }

    @Override
    @Transactional
    public void viewProduct(Integer id) {
        productMapper.updateViewCount(id);
    }
}