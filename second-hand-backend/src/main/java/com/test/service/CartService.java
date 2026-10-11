package com.test.service;

import com.test.entity.Cart;
import java.util.List;

public interface CartService {
    boolean addToCart(Integer userId, Integer productId);
    boolean removeFromCart(Integer id);
    boolean removeFromCartByUserAndProduct(Integer userId, Integer productId);
    Cart findByUserIdAndProductId(Integer userId, Integer productId);
    List<Cart> findByUserId(Integer userId);
    int countByUserId(Integer userId);
}
