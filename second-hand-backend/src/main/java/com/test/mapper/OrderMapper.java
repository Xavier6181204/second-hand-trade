package com.test.mapper;

import com.test.entity.Order;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface OrderMapper {
    Order findById(Integer id);
    Order findByOrderNo(String orderNo);
    List<Order> findByBuyerId(Integer buyerId);
    List<Order> findBySellerId(Integer sellerId);
    int insert(Order order);
    int update(Order order);
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);
}
