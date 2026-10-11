package com.test.service.impl;

import com.test.entity.Cart;
import com.test.entity.Product;
import com.test.mapper.CartMapper;
import com.test.mapper.ProductMapper;
import com.test.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    @Transactional
    public boolean addToCart(Integer userId, Integer productId) {
        // 检查商品是否存在且出售中
        Product product = productMapper.findById(productId);
        if (product == null || product.getStatus() != 1) {
            return false;
        }
        // 检查是否已在购物车中
        Cart exist = cartMapper.findByUserIdAndProductId(userId, productId);
        if (exist != null) {
            return false;
        }
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setProductId(productId);
        return cartMapper.insert(cart) > 0;
    }

    @Override
    public boolean removeFromCart(Integer id) {
        return cartMapper.deleteById(id) > 0;
    }

    @Override
    public boolean removeFromCartByUserAndProduct(Integer userId, Integer productId) {
        return cartMapper.deleteByUserIdAndProductId(userId, productId) > 0;
    }

    @Override
    public Cart findByUserIdAndProductId(Integer userId, Integer productId) {
        return cartMapper.findByUserIdAndProductId(userId, productId);
    }

    @Override
    public List<Cart> findByUserId(Integer userId) {
        return cartMapper.findByUserId(userId);
    }

    @Override
    public int countByUserId(Integer userId) {
        return cartMapper.countByUserId(userId);
    }
}
