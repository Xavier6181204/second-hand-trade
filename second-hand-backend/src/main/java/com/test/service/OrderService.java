package com.test.service;

import com.test.entity.Order;
import java.util.List;

public interface OrderService {
    Order findById(Integer id);
    Order findByOrderNo(String orderNo);
    List<Order> findByBuyerId(Integer buyerId);
    List<Order> findBySellerId(Integer sellerId);
    Order createOrder(Integer productId, Integer buyerId, String remark);
    boolean updateStatus(Integer id, Integer status);
}
