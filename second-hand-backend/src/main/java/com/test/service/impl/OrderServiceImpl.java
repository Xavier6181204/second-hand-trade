package com.test.service.impl;

import com.test.entity.Order;
import com.test.entity.Product;
import com.test.mapper.OrderMapper;
import com.test.mapper.ProductMapper;
import com.test.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductMapper productMapper;

    private String generateOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String prefix = sdf.format(new Date());
        Random random = new Random();
        int suffix = random.nextInt(1000);
        return prefix + String.format("%03d", suffix);
    }

    @Override
    public Order findById(Integer id) {
        return orderMapper.findById(id);
    }

    @Override
    public Order findByOrderNo(String orderNo) {
        return orderMapper.findByOrderNo(orderNo);
    }

    @Override
    public List<Order> findByBuyerId(Integer buyerId) {
        return orderMapper.findByBuyerId(buyerId);
    }

    @Override
    public List<Order> findBySellerId(Integer sellerId) {
        return orderMapper.findBySellerId(sellerId);
    }

    @Override
    @Transactional
    public Order createOrder(Integer productId, Integer buyerId, String remark) {
        Product product = productMapper.findById(productId);
        if (product == null || product.getStatus() != 1) {
            throw new RuntimeException("商品不存在或已下架");
        }

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setProductId(productId);
        order.setBuyerId(buyerId);
        order.setSellerId(product.getSellerId());
        order.setTotalAmount(product.getPrice());
        order.setStatus(1);  // 直接设为"待发货"，跳过"待付款"
        order.setBuyerRemark(remark);

        orderMapper.insert(order);
        productMapper.updateStatus(productId, 3);

        return order;
    }

    @Override
    @Transactional
    public boolean updateStatus(Integer id, Integer status) {
        return orderMapper.updateStatus(id, status) > 0;
    }
}
