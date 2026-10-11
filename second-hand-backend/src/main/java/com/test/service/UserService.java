package com.test.service;

import com.test.entity.User;

public interface UserService {
    User login(String username, String password);
    boolean register(User user);
    User findById(Integer id);
    boolean updateUser(User user);
    boolean updateStatus(Integer id, Integer status);
}
