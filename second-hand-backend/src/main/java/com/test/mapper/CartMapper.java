package com.test.mapper;

import com.test.entity.Cart;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface CartMapper {
    int insert(Cart cart);
    int deleteById(Integer id);
    int deleteByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);
    Cart findByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);
    List<Cart> findByUserId(Integer userId);
    int countByUserId(Integer userId);
}
