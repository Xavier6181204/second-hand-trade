package com.test.mapper;

import com.test.entity.Product;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ProductMapper {

    Product findById(Integer id);

    // 前台首页使用：只显示 status=1（出售中）
    List<Product> findList(@Param("categoryId") Integer categoryId,
                           @Param("keyword") String keyword,
                           @Param("status") Integer status);

    // 后台管理使用：显示所有商品
    List<Product> findAllList(@Param("categoryId") Integer categoryId,
                              @Param("keyword") String keyword);

    List<Product> findBySellerId(Integer sellerId);

    int insert(Product product);

    int update(Product product);

    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    int updateViewCount(Integer id);
}