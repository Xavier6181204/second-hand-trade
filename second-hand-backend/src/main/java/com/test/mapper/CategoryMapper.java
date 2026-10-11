package com.test.mapper;

import com.test.entity.Category;
import java.util.List;

public interface CategoryMapper {
    List<Category> findAll();
    Category findById(Integer id);
    int insert(Category category);
    int update(Category category);
    int deleteById(Integer id);
}