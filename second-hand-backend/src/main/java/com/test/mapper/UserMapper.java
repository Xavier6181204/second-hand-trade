package com.test.mapper;

import com.test.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    User findByUsername(String username);
    User findById(Integer id);
    int insert(User user);
    int update(User user);
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);
}